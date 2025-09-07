package com.company.books2trees.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.repos.BookRepository
import com.company.books2trees.repos.BookRepository.Companion.DEFAULT_GENRE
import com.company.books2trees.repos.BookRepository.GenreList
import com.company.books2trees.utils.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _result = MutableLiveData<ResultViewState>()
    val result: LiveData<ResultViewState>
        get() = _result

    private val _selectedFilter = MutableLiveData(DEFAULT_GENRE)
    val selectedFilter: LiveData<String>
        get() = _selectedFilter

    val genreList = GenreList.toList()


    private var query: String? = null

    init {
        onFilterClicked()
    }

    fun search(query: String) {
        this.query = query
        _result.value = ResultViewState.Loading
        _selectedFilter
        viewModelScope.launch {
            _result.postValue(
                try {
                    val result = BookRepository.search(
                        query,
                        selectedFilter.value)
                    ResultViewState.Content(result)
                } catch (t: Throwable) {
                    ResultViewState.Error(t)
                }
            )
        }
    }

    private fun addRecentItem(model: BookModel) {

    }

    fun onBookClicked(model: BookModel) {
        addRecentItem(model)
    }

    fun onFilterClicked() {
        viewModelScope.launch {
            DataStore.getSearchFilter(getApplication()).collectLatest {
                _selectedFilter.postValue(it)
            }
        }

    }

    fun onFilterItemClicked(position: Int) {
        _selectedFilter.value = genreList[position]
    }

    fun applyFilter() {
        viewModelScope.launch {
            _selectedFilter.value?.let {
                DataStore.setSearchFilter(
                    getApplication(), it
                )
            }
        }
//      To add this feature, the caching need to remove
        query?.let{ search(it) }
    }


}