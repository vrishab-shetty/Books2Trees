package com.company.books2trees.data.repository

import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.data.local.datasource.api.LibraryLocalDataSource
import com.company.books2trees.data.mapper.toLibraryBookModel
import com.company.books2trees.data.mapper.toLibraryItemEntity
import com.company.books2trees.domain.model.LibraryBookModel
import com.company.books2trees.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.map

class LibraryRepositoryImpl(private val localDataSource: LibraryLocalDataSource) : LibraryRepository {

    override fun getAllLibraryBooks() = localDataSource.getAllLibraryItems().map {
        it.map(LibraryBookEntity::toLibraryBookModel)
    }

    override suspend fun addLibraryBook(
        item: LibraryBookModel
    ) {
        val entity = item.toLibraryItemEntity()
        localDataSource.insertLibraryItem(entity)
    }

    override suspend fun removeLibraryBook(id: String) = localDataSource.deleteLibraryItem(id)

    override suspend fun updateLibraryBook(item: LibraryBookModel) =
        localDataSource.updateLibraryItem(item.toLibraryItemEntity())
}