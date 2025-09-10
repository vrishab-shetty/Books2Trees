package com.company.books2trees.domain.repository

import com.company.books2trees.data.local.model.LibraryItem
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.profile.LibraryPageItem
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    suspend fun insertLibraryBook(model: BookModel, categoryId: LibraryPageItem.CategoryId)
    suspend fun deleteLibraryBook(id: String)
    suspend fun updateLibraryBook(item: LibraryItem)
    fun getAllLibraryBooksFlow(): Flow<List<LibraryItem>>
}