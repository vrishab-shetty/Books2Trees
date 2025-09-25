package com.company.books2trees.data.repository

import com.company.books2trees.data.local.datasource.api.RecentBooksLocalDataSource
import com.company.books2trees.data.mapper.toBookModelFromRecent
import com.company.books2trees.data.mapper.toRecentItemEntity
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.RecentBooksRepository
import kotlinx.coroutines.flow.map

class RecentBooksRepositoryImpl(private val localDataSource: RecentBooksLocalDataSource) : RecentBooksRepository {

    override suspend fun addRecentBook(model: BookModel) {
        val entity = model.toRecentItemEntity()
        localDataSource.insertRecentItem(entity)
    }

    override suspend fun removeRecentBook(id: String) = localDataSource.deleteRecentItem(id)

    override fun getRecentBooks() = localDataSource.getAllRecentItems().map { recentItems ->
        recentItems.map {
            it.toBookModelFromRecent()
        }
    }
}