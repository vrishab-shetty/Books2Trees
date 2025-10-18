package com.company.books2trees.data.local.datasource.impl

import com.company.books2trees.data.local.database.dao.LibraryBookDao
import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.data.local.datasource.api.LibraryLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LibraryLocalDataSourceImpl(private val libraryBookDao: LibraryBookDao) : LibraryLocalDataSource {
    override suspend fun insertLibraryItem(item: LibraryBookEntity) = withContext(Dispatchers.IO) {
        libraryBookDao.insert(item)
    }

    override suspend fun deleteLibraryItem(id: String) = withContext(Dispatchers.IO) {
        libraryBookDao.delete(id)
    }

    override suspend fun updateLibraryItem(item: LibraryBookEntity) = withContext(Dispatchers.IO) {
        libraryBookDao.update(item)
    }

    override fun getAllLibraryItems(): Flow<List<LibraryBookEntity>> {
        return libraryBookDao.getAll()
    }

}