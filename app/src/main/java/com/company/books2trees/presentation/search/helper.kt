package com.company.books2trees.presentation.search

import com.company.books2trees.domain.model.BookModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun toResultFlow(block: suspend () -> List<BookModel>): Flow<ResultViewState> {
    return flow {
        emit(ResultViewState.Loading)
        runCatching {
            block()
        }.onSuccess { result ->
            emit(ResultViewState.Content(result))
        }.onFailure { exception ->
            emit(ResultViewState.Error(exception))
        }
    }
}