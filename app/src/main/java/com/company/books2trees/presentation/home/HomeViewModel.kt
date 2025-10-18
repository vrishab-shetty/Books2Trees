package com.company.books2trees.presentation.home

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.model.CategoryId
import com.company.books2trees.domain.use_case.AddBookToLibraryUseCase
import com.company.books2trees.domain.use_case.AddRecentBookUseCase
import com.company.books2trees.domain.use_case.GetHomePageBooksUseCase
import com.company.books2trees.domain.use_case.GetRecentBooksUseCase
import com.company.books2trees.domain.use_case.GrantReadingTimeUseCase
import com.company.books2trees.domain.use_case.RemoveRecentBookUseCase
import com.company.books2trees.presentation.home.viewState.AdUiState
import com.company.books2trees.presentation.home.viewState.HomeEvent
import com.company.books2trees.presentation.home.viewState.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomePageBooks: GetHomePageBooksUseCase,
    private val getRecentBooks: GetRecentBooksUseCase,
    private val addRecentBook: AddRecentBookUseCase,
    private val removeRecentBook: RemoveRecentBookUseCase,
    private val insertBookToLibrary: AddBookToLibraryUseCase,
    private val grantReadingTimeUseCase: GrantReadingTimeUseCase
) : ViewModel() {

    private val _adState = MutableStateFlow<AdUiState>(AdUiState.Idle)
    val adState: StateFlow<AdUiState> = _adState

    private val _items: MutableStateFlow<HomeViewState> = MutableStateFlow(
        HomeViewState.Loading
    )
    val items: StateFlow<HomeViewState>
        get() = _items

    init {
        fetchHomeScreenContent()
    }

    private fun fetchHomeScreenContent() {
        viewModelScope.launch {
            _items.value = HomeViewState.Loading
            try {
                // Perform the one-shot fetch for the main content
                val mainItemsMap = getHomePageBooks()

                // Now, collect the flow of recent items and combine it with the main content
                getRecentBooks()
                    .catch { exception ->
                        // If recents flow fails, still show main content
                        _items.value = HomeViewState.Content(mainItemsMap, emptyList())
                        // Optionally log the exception
                    }
                    .collect { recentItems ->
                        // On each update to recents, update the full state
                        _items.value = HomeViewState.Content(mainItemsMap, recentItems)
                    }

            } catch (e: Exception) {
                // This catches errors from getHomePageBooks()
                _items.value = HomeViewState.Error(e)
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OpenBook -> onBookOpened(event.book, event.activity)
            is HomeEvent.AddToLibrary -> insertToLibrary(event.book, event.categoryId)
            is HomeEvent.RemoveFromRecents -> onRemoveClicked(event.book)
            is HomeEvent.ExpandList -> {}
        }
    }

    private fun onBookOpened(model: BookModel, activity: Activity) {
        viewModelScope.launch {
            _adState.value = AdUiState.Loading

            val success = grantReadingTimeUseCase(activity)
            _adState.value =
                if (success) AdUiState.RewardGranted(model.url) else AdUiState.Error("Ad failed to show")

            addRecentBook(model)
        }

    }

    fun onAdFlowComplete() {
        _adState.value = AdUiState.Idle
    }

    private fun onRemoveClicked(model: BookModel) {
        viewModelScope.launch {
            removeRecentBook(model.id)
        }
    }

    private fun insertToLibrary(model: BookModel, categoryId: CategoryId) {
        viewModelScope.launch {
            insertBookToLibrary(model, categoryId)
        }
    }

}