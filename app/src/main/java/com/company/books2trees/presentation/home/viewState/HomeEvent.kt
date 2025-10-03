package com.company.books2trees.presentation.home.viewState

import android.app.Activity
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.model.CategoryId

sealed interface HomeEvent {
    data class OpenBook(val book: BookModel, val activity: Activity) : HomeEvent
    data class AddToLibrary(val book: BookModel, val categoryId: CategoryId) : HomeEvent
    data class RemoveFromRecents(val book: BookModel) : HomeEvent
    data class ExpandList(val title: String, val books: List<BookModel>) : HomeEvent
}