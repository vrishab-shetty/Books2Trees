package com.company.books2trees.domain.repository

import com.company.books2trees.domain.model.LibraryBookModel
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    suspend fun addLibraryBook(item: LibraryBookModel)
    suspend fun removeLibraryBook(id: String)
    suspend fun updateLibraryBook(item: LibraryBookModel)
    fun getAllLibraryBooks(): Flow<List<LibraryBookModel>>
}