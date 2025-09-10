package com.company.books2trees.data.local

import com.company.books2trees.data.local.model.RecentItem
import kotlinx.coroutines.flow.Flow

class BookLocalDataSource(private val recentBookDao: RecentBookDao) {

    suspend fun insertRecentItem(item: RecentItem) {
        recentBookDao.insert(item)
    }

    suspend fun deleteRecentItem(id: String) {
        recentBookDao.delete(id)
    }

    fun getAllRecentItems(): Flow<List<RecentItem>> {
        return recentBookDao.getAll()
    }
}