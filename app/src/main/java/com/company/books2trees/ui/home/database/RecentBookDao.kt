package com.company.books2trees.ui.home.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentBookDao {

    @Query("SELECT * FROM recent ORDER BY lastAccessed DESC LIMIT 5")
    fun getAll(): Flow<List<RecentItem>>

    @Query("SELECT * FROM recent where id = :id")
    suspend fun get(id: String): RecentItem

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: RecentItem)

    @Query("DELETE from recent where id = :id")
    suspend fun delete(id: String)
}