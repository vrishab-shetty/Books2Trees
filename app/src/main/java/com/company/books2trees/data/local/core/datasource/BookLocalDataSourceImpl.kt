package com.company.books2trees.data.local.core.datasource

import com.company.books2trees.data.local.library.api.LibraryDao
import com.company.books2trees.data.local.library.model.LibraryItem
import com.company.books2trees.data.local.recent.api.RecentBookDao
import com.company.books2trees.data.local.recent.model.RecentItem
import kotlinx.coroutines.flow.Flow

class BookLocalDataSourceImpl(
    private val recentBookDao: RecentBookDao,
    private val libraryBookDoa: LibraryDao
) : BookLocalDataSource {

    override suspend fun insertRecentItem(item: RecentItem) {
        recentBookDao.insert(item)
    }

    override suspend fun deleteRecentItem(id: String) {
        recentBookDao.delete(id)
    }

    override fun getAllRecentItems(): Flow<List<RecentItem>> {
        return recentBookDao.getAll()
    }

    override suspend fun insertLibraryItem(item: LibraryItem) {
        libraryBookDoa.insert(item)
    }

    override suspend fun deleteLibraryItem(id: String) {
        libraryBookDoa.delete(id)
    }

    override suspend fun updateLibraryItem(item: LibraryItem) {
        libraryBookDoa.update(item)
    }

    override fun getAllLibraryItems(): Flow<List<LibraryItem>> {
        return libraryBookDoa.getAll()
    }
}