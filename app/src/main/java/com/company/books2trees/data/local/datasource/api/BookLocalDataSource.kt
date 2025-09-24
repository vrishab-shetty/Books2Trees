package com.company.books2trees.data.local.datasource.api

import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.data.local.database.model.RecentBookEntity
import kotlinx.coroutines.flow.Flow

interface BookLocalDataSource {

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

    /**
     * Inserts a book into the user's library.
     */
    suspend fun insertLibraryItem(item: LibraryBookEntity)

    /**
     * Deletes a book from the user's library by its ID.
     */
    suspend fun deleteLibraryItem(id: String)

    /**
     * Updates an existing item in the user's library.
     */
    suspend fun updateLibraryItem(item: LibraryBookEntity)

    /**
     * Gets a reactive stream of all books in the user's library.
     */
    fun getAllLibraryItems(): Flow<List<LibraryBookEntity>>
}