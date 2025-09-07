package com.company.books2trees.ui.home.adapter

import com.company.books2trees.ui.home.HomePageList
import com.company.books2trees.ui.models.BookModel

sealed class HomePageListItem {

    abstract val id: String

    data class RecentBooks(val books: List<BookModel>): HomePageListItem() {
        override val id: String = "recent_books_header"
    }

    data class BookList(val list: HomePageList): HomePageListItem() {
        override val id: String = list.name.toString()
    }
}