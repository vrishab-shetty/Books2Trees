package com.company.books2trees.ui.home.viewState

import com.company.books2trees.ui.models.BookModel

sealed class HomeViewState {
    data object Loading: HomeViewState()
    data class Content(val items: Map<Int, List<BookModel>>): HomeViewState()
    data class Error(val throwable: Throwable): HomeViewState()
}