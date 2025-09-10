package com.company.books2trees.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.use_case.AddRecentBookUseCase
import com.company.books2trees.domain.use_case.GetGenresUseCase
import com.company.books2trees.domain.use_case.GetSearchFilterUseCase
import com.company.books2trees.domain.use_case.SearchBooksUseCase
import com.company.books2trees.domain.use_case.SetSearchFilterUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    // The ViewModel now depends only on Use Cases
    private val searchBooks: SearchBooksUseCase,
    private val addRecentBook: AddRecentBookUseCase,
    getGenres: GetGenresUseCase,
    getSearchFilter: GetSearchFilterUseCase, // Used in init
    private val setSearchFilter: SetSearchFilterUseCase
) : ViewModel() {

    private val _result = MutableStateFlow<ResultViewState>(ResultViewState.Loading)
    val result: StateFlow<ResultViewState> get() = _result.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> get() = _selectedFilter.asStateFlow()

    // The list is now provided by a use case
    val genreList: List<String> = getGenres()

    // A state flow to hold the current search query
    private val query = MutableStateFlow("")

    init {
        // Load the last used filter from DataStore
        viewModelScope.launch {
            getSearchFilter().onEach { savedFilter ->
                _selectedFilter.value = savedFilter
            }.launchIn(this)
        }


        query.combine(selectedFilter) { query, filter ->
            Pair(query, filter)
        }.flatMapLatest { (query, filter) ->
            flow {
                if (query.isBlank()) {
                    emit(ResultViewState.Loading)
                    return@flow
                }
                emit(ResultViewState.Loading)
                try {
                    val books = searchBooks(query, filter)
                    emit(ResultViewState.Content(books))
                } catch (e: Exception) {
                    emit(ResultViewState.Error(e))
                }
            }
        }
            .onEach { state -> _result.value = state }
            .launchIn(viewModelScope)
    }

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery
    }

    fun onBookClicked(model: BookModel) {
        viewModelScope.launch {
            addRecentBook(model)
        }
    }

    fun onFilterItemClicked(position: Int) {
        _selectedFilter.value = genreList[position]
    }

    fun applyFilter() {
        viewModelScope.launch {
            setSearchFilter(_selectedFilter.value)
        }
    }
}