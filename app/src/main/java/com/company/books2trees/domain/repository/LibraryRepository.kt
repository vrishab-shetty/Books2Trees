package com.company.books2trees.domain.repository

import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.profile.LibraryPageItem
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    suspend fun addLibraryBook(model: BookModel, categoryId: LibraryPageItem.CategoryId)
    suspend fun removeLibraryBook(id: String)
    suspend fun updateLibraryBook(item: LibraryBookEntity)
    fun getAllLibraryBooks(): Flow<List<LibraryBookEntity>>
}