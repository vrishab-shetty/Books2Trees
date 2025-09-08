package com.company.books2trees.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.presentation.home.viewState.HomeViewState
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.profile.LibraryPageItem
import com.company.books2trees.data.repository.BookRepository
import com.company.books2trees.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


class HomeViewModel(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _items: MutableStateFlow<HomeViewState> = MutableStateFlow(
        HomeViewState.Loading
    )
    val items: StateFlow<HomeViewState>
        get() = _items

    private val _recentList = bookRepository.loadRecent()

    init {
        fetchCombinedItems()
    }

    private fun fetchCombinedItems() {
        viewModelScope.launch {
            combine(_recentList, bookRepository.fetchItemsFlow()) { recent, mainItemsResult ->
                if (mainItemsResult.isSuccess) {
                    HomeViewState.Content(
                        mainItemsResult.getOrThrow(),
                        recent
                    )
                } else {
                    HomeViewState.Error(mainItemsResult.exceptionOrNull()!!)
                }
            }.catch { e -> emit(HomeViewState.Error(e)) }
                .collect { combinedState ->
                    _items.value = combinedState
                }
        }
    }

    private fun addRecentItem(model: BookModel) {
        viewModelScope.launch {
            try {
                bookRepository.insertRecent(model)
            } catch (t: Throwable) {
                error("Insert Operation Unsuccessful ${t.message}")
            }
        }
    }

    fun onBookClicked(model: BookModel) {
        addRecentItem(model)
    }

    fun onRemoveClicked(model: BookModel) {

        viewModelScope.launch {
            try {
                bookRepository.deleteRecent(model.id)
            } catch (t: Throwable) {
                error("Delete Operation Unsuccessful")
            }
        }

    }

    fun insertToLibrary(model: BookModel, categoryId: LibraryPageItem.CategoryId) {
        viewModelScope.launch {
            try {
                libraryRepository.insert(model, categoryId)
            } catch (t: Throwable) {
                error("Insert Operation Unsuccessful")
            }
        }
    }

}