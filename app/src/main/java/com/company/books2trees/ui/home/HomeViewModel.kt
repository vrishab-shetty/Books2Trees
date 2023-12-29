package com.company.books2trees.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.company.books2trees.repos.BookRepository
import com.company.books2trees.repos.LibraryRepository
import com.company.books2trees.ui.dataclass.RecentList
import com.company.books2trees.ui.home.database.RecentItem
import com.company.books2trees.ui.home.viewState.HomeViewState
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.profile.LibraryPageItem
import com.company.books2trees.ui.profile.database.LibraryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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

    private val _recentList = bookRepository.loadRecent().map { models ->
        RecentList[5, models].getItems()
    }
    val recentList: LiveData<List<RecentItem>>
        get() = _recentList.asLiveData()

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _items.value = HomeViewState.Loading
                val result = bookRepository.fetchItems()
                _items.value = HomeViewState.Content(result)
            } catch (t: Throwable) {
                _items.value = HomeViewState.Error(t)
            }
        }
    }

    private fun addRecentItem(model: BookModel) {
        viewModelScope.launch(Dispatchers.Main) {
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

        viewModelScope.launch(Dispatchers.Main) {
            try {
                bookRepository.deleteRecent(model.id)
            } catch (t: Throwable) {
                error("Delete Operation Unsuccessful")
            }
        }

    }

    fun insertToLibrary(model: BookModel, categoryId: LibraryPageItem.CategoryId) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                libraryRepository.insert(model, categoryId)
            } catch (t: Throwable) {
                error("Insert Operation Unsuccessful")
            }
        }
    }

}