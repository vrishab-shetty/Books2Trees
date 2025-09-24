package com.company.books2trees.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.company.books2trees.data.local.database.model.LibraryBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryBookDao {

    @Query("SELECT * FROM library")
    fun getAll(): Flow<List<LibraryBookEntity>>

    @Query("SELECT * FROM library where id = :id")
    suspend fun get(id: String): LibraryBookEntity

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(entity: LibraryBookEntity)

    @Query("DELETE from library where id = :id")
    suspend fun delete(id: String)

    @Update
    suspend fun update(entity: LibraryBookEntity)
}