package com.company.books2trees.data.local.datasource.api

import com.company.books2trees.data.local.database.model.RecentBookEntity
import kotlinx.coroutines.flow.Flow

interface RecentBooksLocalDataSource {
    /**
     * Inserts a book into the recently viewed list.
     */
    suspend fun insertRecentItem(item: RecentBookEntity)

    /**
     * Deletes a book from the recently viewed list by its ID.
     */
    suspend fun deleteRecentItem(id: String)

    /**
     * Gets a reactive stream of all recently viewed books.
     */
    fun getAllRecentItems(): Flow<List<RecentBookEntity>>

}