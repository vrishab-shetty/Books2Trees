package com.company.books2trees.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.domain.ads.AdManager
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.use_case.AddRecentBookUseCase
import com.company.books2trees.domain.use_case.GetHomePageBooksUseCase
import com.company.books2trees.domain.use_case.GetRecentBooksUseCase
import com.company.books2trees.domain.use_case.InsertBookToLibraryUseCase
import com.company.books2trees.domain.use_case.RemoveRecentBookUseCase
import com.company.books2trees.presentation.home.viewState.HomeViewState
import com.company.books2trees.presentation.profile.LibraryPageItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class NavigationEvent {
    data class ShowAdAndOpenUrl(val url: String) : NavigationEvent()
    data class ShowToastMessage(val message: String) : NavigationEvent()
}

class HomeViewModel(
    private val getHomePageBooks: GetHomePageBooksUseCase,
    private val getRecentBooks: GetRecentBooksUseCase,
    private val addRecentBook: AddRecentBookUseCase,
    private val removeRecentBook: RemoveRecentBookUseCase,
    private val insertBookToLibrary: InsertBookToLibraryUseCase,
    private val adManager: AdManager
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

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

    fun onBookClicked(model: BookModel) {
        viewModelScope.launch {
            addRecentBook(model)

            val url = model.url ?: return@launch

            if (adManager.isAdReady()) {
                _navigationEvent.emit(NavigationEvent.ShowAdAndOpenUrl(url))
            } else {
                _navigationEvent.emit(NavigationEvent.ShowToastMessage("Ad not ready. Try again later."))
            }
        }
    }

    fun onRemoveClicked(model: BookModel) {
        viewModelScope.launch {
            removeRecentBook(model.id)
        }
    }

    fun insertToLibrary(model: BookModel, categoryId: LibraryPageItem.CategoryId) {
        viewModelScope.launch {
            insertBookToLibrary(model, categoryId)
        }
    }

}