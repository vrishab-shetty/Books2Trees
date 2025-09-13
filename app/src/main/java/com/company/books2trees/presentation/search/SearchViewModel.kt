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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _selectedFilter = MutableStateFlow("")
    val selectedFilter: StateFlow<String> get() = _selectedFilter.asStateFlow()

    private val _genreList = MutableStateFlow<List<String>>(emptyList())
    val genreList: StateFlow<List<String>> = _genreList.asStateFlow()

    private val query = MutableStateFlow("")

    val result: StateFlow<ResultViewState> = query
        .combine(selectedFilter) { query, filter -> Pair(query, filter) }
        .flatMapLatest { (query, filter) ->
            if (query.isBlank()) {
                flow<ResultViewState> {
                    emit(ResultViewState.Content(emptyList()))
                }
            } else {
                flow {
                    emit(ResultViewState.Loading)
                    try {
                        val books = searchBooks(query, filter)
                        emit(ResultViewState.Content(books))
                    } catch (e: Exception) {
                        emit(ResultViewState.Error(e))
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ResultViewState.Loading // This will now compile correctly
        )

    init {
        viewModelScope.launch {
            _genreList.value = getGenres()

            getSearchFilter().onEach { savedFilter ->
                _selectedFilter.value = savedFilter
            }.launchIn(this)
        }
    }

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery
    }

    fun onBookClicked(model: BookModel) {
        viewModelScope.launch {
            addRecentBook(model)
            model.url?.let {
                _navigationEvent.emit(it)
            }
        }
    }

    fun onFilterItemClicked(filter: String) {
        _selectedFilter.value = filter
    }

    fun applyFilter() {
        viewModelScope.launch {
            setSearchFilter(_selectedFilter.value)
        }
    }
}