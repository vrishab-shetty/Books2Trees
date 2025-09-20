package com.company.books2trees.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.data.local.library.model.LibraryItem
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.use_case.AddRecentBookUseCase
import com.company.books2trees.domain.use_case.DeleteLibraryBookUseCase
import com.company.books2trees.domain.use_case.GetProfileContentUseCase
import com.company.books2trees.domain.use_case.UpdateLibraryItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileContent: GetProfileContentUseCase,
    private val addRecentBook: AddRecentBookUseCase,
    private val deleteLibraryBook: DeleteLibraryBookUseCase,
    private val updateLibraryItem: UpdateLibraryItemUseCase
) : ViewModel() {


    private val _pages: MutableStateFlow<ProfileViewState> =
        MutableStateFlow(ProfileViewState.Loading)
    val pages: StateFlow<ProfileViewState>
        get() = _pages.asStateFlow()


    private var fullPageData: List<LibraryPageItem> = emptyList()


    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Call the use case to get the structured data flow
            getProfileContent()
                .catch { _pages.value = ProfileViewState.Error(it) }
                .collectLatest { content ->
                    fullPageData = content // Cache the full list
                    _pages.value = ProfileViewState.Content(content)
                }
        }
    }

    fun onBookClicked(model: BookModel) {
        viewModelScope.launch {
            addRecentBook(model)
        }
    }

    fun onDeleteBookClicked(id: String) {
        viewModelScope.launch {
            deleteLibraryBook(id)
        }
    }

    fun onChangedCategory(item: LibraryItem) {
        viewModelScope.launch {
            updateLibraryItem(item)
        }
    }

    fun onSortClicked(position: Int) {
        val currentContent = (_pages.value as? ProfileViewState.Content)?.list ?: return
        val sortedList = currentContent.toMutableList()

        sortedList[position] = sortedList[position].copy(
            items = sortedList[position].items.sortedBy { it.title }
        )
        _pages.value = ProfileViewState.Content(list = sortedList)
    }

    fun onSearch(position: Int, query: String?) {
        if (query.isNullOrBlank()) {
            onSearchClosed()
            return
        }

        val filteredList = fullPageData.toMutableList()
        val lowercaseQuery = query.trim().lowercase()

        filteredList[position] = filteredList[position].copy(
            items = fullPageData[position].items.filter {
                it.title.lowercase().contains(lowercaseQuery)
            }
        )
        _pages.value = ProfileViewState.Content(list = filteredList)
    }

    fun onSearchClosed() {
        // Reset the UI with the original, cached list
        _pages.value = ProfileViewState.Content(list = fullPageData)
    }

}
