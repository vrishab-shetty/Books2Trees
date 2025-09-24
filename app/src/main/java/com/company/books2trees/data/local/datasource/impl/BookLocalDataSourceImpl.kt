package com.company.books2trees.data.local.datasource.impl

import com.company.books2trees.data.local.database.dao.LibraryBookDao
import com.company.books2trees.data.local.database.dao.RecentBookDao
import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.data.local.database.model.RecentBookEntity
import com.company.books2trees.data.local.datasource.api.BookLocalDataSource
import kotlinx.coroutines.flow.Flow

class BookLocalDataSourceImpl(
    private val recentBookDao: RecentBookDao,
    private val libraryBookDoa: LibraryBookDao
) : BookLocalDataSource {

    override suspend fun insertRecentItem(item: RecentBookEntity) {
        recentBookDao.insert(item)
    }

    override suspend fun deleteRecentItem(id: String) {
        recentBookDao.delete(id)
    }

    override fun getAllRecentItems(): Flow<List<RecentBookEntity>> {
        return recentBookDao.getAll()
    }

    override suspend fun insertLibraryItem(item: LibraryBookEntity) {
        libraryBookDoa.insert(item)
    }

    override suspend fun deleteLibraryItem(id: String) {
        libraryBookDoa.delete(id)
    }

    override suspend fun updateLibraryItem(item: LibraryBookEntity) {
        libraryBookDoa.update(item)
    }

    override fun getAllLibraryItems(): Flow<List<LibraryBookEntity>> {
        return libraryBookDoa.getAll()
    }
}