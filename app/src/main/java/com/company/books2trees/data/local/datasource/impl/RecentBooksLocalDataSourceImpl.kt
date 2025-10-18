package com.company.books2trees.data.local.datasource.impl

import com.company.books2trees.data.local.database.dao.RecentBookDao
import com.company.books2trees.data.local.database.model.RecentBookEntity
import com.company.books2trees.data.local.datasource.api.RecentBooksLocalDataSource
import kotlinx.coroutines.flow.Flow

class RecentBooksLocalDataSourceImpl(private val recentBookDao: RecentBookDao) : RecentBooksLocalDataSource {
    override suspend fun insertRecentItem(item: RecentBookEntity) {
        recentBookDao.insert(item)
    }

    override suspend fun deleteRecentItem(id: String) {
        recentBookDao.delete(id)
    }

    override fun getAllRecentItems(): Flow<List<RecentBookEntity>> {
        return recentBookDao.getAll()
    }
}