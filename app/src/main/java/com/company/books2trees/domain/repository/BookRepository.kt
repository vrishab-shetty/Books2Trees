package com.company.books2trees.domain.repository

import com.company.books2trees.domain.model.BookModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    suspend fun getTrendingBooks(): List<BookModel>
    suspend fun getBooksBySubject(subject: String, limit: Int): List<BookModel>
    suspend fun searchBooks(query: String, filter: String?): List<BookModel>
    suspend fun addRecentBook(model: BookModel)
    suspend fun removeRecentBook(id: String)
    fun getRecentBooksFlow(): Flow<List<BookModel>>

    fun getSearchFilterFlow(): Flow<String>
    suspend fun setSearchFilter(filter: String)
    fun getGenres(): List<String>
}