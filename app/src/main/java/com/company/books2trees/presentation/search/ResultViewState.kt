package com.company.books2trees.presentation.search

import com.company.books2trees.domain.model.BookModel

sealed class ResultViewState {
    data object Loading : ResultViewState()
    data class Content(val list: List<BookModel>) : ResultViewState()
    data class Error(val throwable: Throwable) : ResultViewState()
}
