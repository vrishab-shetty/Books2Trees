package com.company.books2trees.domain.repository

import com.company.books2trees.domain.model.BookModel

interface BookSearchRepository {
    // One shot operations
    suspend fun getTrendingBooks(): List<BookModel>
    suspend fun getBooksBySubject(subject: String, limit: Int): List<BookModel>
    suspend fun searchBooks(query: String, filter: String): List<BookModel>
}