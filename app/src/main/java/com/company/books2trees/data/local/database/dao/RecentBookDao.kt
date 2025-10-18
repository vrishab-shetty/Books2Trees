package com.company.books2trees.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.company.books2trees.data.local.database.model.RecentBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentBookDao {

    @Query("SELECT * FROM recent ORDER BY lastAccessed DESC LIMIT 5")
    fun getAll(): Flow<List<RecentBookEntity>>

    @Query("SELECT * FROM recent where id = :id")
    suspend fun get(id: String): RecentBookEntity

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(item: RecentBookEntity)

    @Query("DELETE from recent where id = :id")
    suspend fun delete(id: String)
}