package com.company.books2trees.data.repository

import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.data.local.datasource.api.LibraryLocalDataSource
import com.company.books2trees.data.mapper.toLibraryItemEntity
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.LibraryRepository
import com.company.books2trees.presentation.profile.LibraryPageItem

class LibraryRepositoryImpl(private val localDataSource: LibraryLocalDataSource) : LibraryRepository {

    override fun getAllLibraryBooks() = localDataSource.getAllLibraryItems()

    override suspend fun addLibraryBook(
        model: BookModel, categoryId: LibraryPageItem.CategoryId
    ) {
        val entity = model.toLibraryItemEntity(categoryId)
        localDataSource.insertLibraryItem(entity)
    }

    override suspend fun removeLibraryBook(id: String) = localDataSource.deleteLibraryItem(id)

    override suspend fun updateLibraryBook(item: LibraryBookEntity) =
        localDataSource.updateLibraryItem(item)
}