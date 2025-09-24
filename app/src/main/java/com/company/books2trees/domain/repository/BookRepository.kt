package com.company.books2trees.domain.repository

import com.company.books2trees.data.local.library.model.LibraryItem
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.profile.LibraryPageItem
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    // One shot operations
    suspend fun getTrendingBooks(): List<BookModel>
    suspend fun getBooksBySubject(subject: String, limit: Int): List<BookModel>
    suspend fun searchBooks(query: String, filter: String): List<BookModel>
    suspend fun addRecentBook(model: BookModel)
    suspend fun removeRecentBook(id: String)
    suspend fun setSearchFilter(filter: String)
    suspend fun getGenres(): List<String>
    suspend fun insertLibraryBook(model: BookModel, categoryId: LibraryPageItem.CategoryId)
    suspend fun deleteLibraryBook(id: String)
    suspend fun updateLibraryBook(item: LibraryItem)

    // Flow operations
    fun getRecentBooksFlow(): Flow<List<BookModel>>
    fun getSearchFilterFlow(): Flow<String>
    fun getAllLibraryBooksFlow(): Flow<List<LibraryItem>>
}