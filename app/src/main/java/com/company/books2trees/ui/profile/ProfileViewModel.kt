package com.company.books2trees.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.repos.BookRepository
import com.company.books2trees.repos.LibraryRepository
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.profile.LibraryPageItem.CategoryId
import com.company.books2trees.ui.profile.database.LibraryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository
) : ViewModel() {


    private val _pages: MutableStateFlow<ProfileViewState> =
        MutableStateFlow(ProfileViewState.Loading)
    val pages: StateFlow<ProfileViewState>
        get() = _pages.asStateFlow()


    private var data: List<LibraryPageItem>? = getPageDataOrNull()


    init {
        loadData()
    }

    private fun getPageDataOrNull() =
        if (_pages.value is ProfileViewState.Content)
            (_pages.value as ProfileViewState.Content).list
        else null

    private fun loadData() {
        viewModelScope.launch {
            libraryRepository.load().map { models ->
                ProfileViewState.Content(
                    // Used CategoryId values to maintain the order everytime receiving the records
                    CategoryId.entries
                        .associateWith { categoryId -> models.filter { it.categoryId == categoryId } }
                        .map { LibraryPageItem(it.key, it.value) }
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ProfileViewState.Loading
            ).collectLatest {
                _pages.value = it
                data = getPageDataOrNull() ?: emptyList()
            }

        }
    }

    fun onBookClicked(model: BookModel) {
        viewModelScope.launch {
            try {
                bookRepository.insertRecent(model)
            } catch (t: Throwable) {
                error("Insert Operation Unsuccessful ${t.message}")
            }
        }
    }

    fun onDeleteBookClicked(id: String) {
        viewModelScope.launch {
            try {
                libraryRepository.delete(id)
            } catch (t: Throwable) {
                error("Delete Operation Unsuccessful")
            }
        }
    }

    fun onSortClicked(position: Int) {
        getPageDataOrNull()?.let { list ->
            val copyList = list.toMutableList()

            copyList[position] = LibraryPageItem(
                copyList[position].categoryId,
                copyList[position].items.sortedBy { it.title }
            )

            _pages.value = ProfileViewState.Content(list = copyList)
        }

    }

    fun updateLibraryItem(item: LibraryItem) {
        viewModelScope.launch {
            try {
                libraryRepository.update(item)
            } catch (t: Throwable) {
                error("Update Operation Unsuccessful")
            }
        }
    }

    fun onSearch(position: Int, query: String?) {
            query?.trim()?.lowercase()?.let {
                data?.let { list ->
                    val copyList = list.toMutableList()

                    copyList[position] = LibraryPageItem(
                        copyList[position].categoryId,
                        copyList[position].items.filter {
                            it.title.lowercase().contains(query)
                        }
                    )

                    _pages.value = ProfileViewState.Content(list = copyList)
                }

            }
    }
    fun onSearchClosed() {
        data?.let {
            _pages.value = ProfileViewState.Content(list = it.toMutableList())
        }
    }

}
