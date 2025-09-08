package com.company.books2trees.presentation.home.adapter

import com.company.books2trees.presentation.home.adapter.HomePageList
import com.company.books2trees.domain.model.BookModel

sealed class HomePageListItem {

    abstract val id: String

    data class RecentBooks(val books: List<BookModel>): HomePageListItem() {
        override val id: String = "recent_books_header"
    }

    data class BookList(val list: HomePageList): HomePageListItem() {
        override val id: String = list.name.toString()
    }
}