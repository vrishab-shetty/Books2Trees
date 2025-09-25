package com.company.books2trees.data.local.datasource.impl

import com.company.books2trees.data.local.database.dao.LibraryBookDao
import com.company.books2trees.data.local.database.dao.RecentBookDao
import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.data.local.database.model.RecentBookEntity
import com.company.books2trees.data.local.datasource.api.BookLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BookLocalDataSourceImpl(
    private val recentBookDao: RecentBookDao,
    private val libraryBookDoa: LibraryBookDao
) : BookLocalDataSource {

    override suspend fun insertRecentItem(item: RecentBookEntity) = withContext(Dispatchers.IO) {
        recentBookDao.insert(item)
    }

    override suspend fun deleteRecentItem(id: String) = withContext(Dispatchers.IO) {
        recentBookDao.delete(id)
    }

    override fun getAllRecentItems(): Flow<List<RecentBookEntity>> {
        return recentBookDao.getAll()
    }

    override suspend fun insertLibraryItem(item: LibraryBookEntity) = withContext(Dispatchers.IO) {
        libraryBookDoa.insert(item)
    }

    override suspend fun deleteLibraryItem(id: String) = withContext(Dispatchers.IO) {
        libraryBookDoa.delete(id)
    }

    override suspend fun updateLibraryItem(item: LibraryBookEntity) = withContext(Dispatchers.IO) {
        libraryBookDoa.update(item)
    }

    override fun getAllLibraryItems(): Flow<List<LibraryBookEntity>> {
        return libraryBookDoa.getAll()
    }
}