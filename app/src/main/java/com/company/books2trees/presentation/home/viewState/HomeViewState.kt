package com.company.books2trees.presentation.home.viewState

import com.company.books2trees.domain.model.BookModel

sealed class HomeViewState {
    data object Loading : HomeViewState()
    data class Content(
        val items: Map<Int, List<BookModel>>,
        val recentItems: List<BookModel> = emptyList()
    ) : HomeViewState()

    data class Error(val throwable: Throwable) : HomeViewState()
}