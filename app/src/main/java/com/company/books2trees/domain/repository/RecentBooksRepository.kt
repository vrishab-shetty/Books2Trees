package com.company.books2trees.domain.repository

import com.company.books2trees.domain.model.BookModel
import kotlinx.coroutines.flow.Flow

interface RecentBooksRepository {
    suspend fun addRecentBook(model: BookModel)
    suspend fun removeRecentBook(id: String)
    fun getRecentBooks(): Flow<List<BookModel>>
}