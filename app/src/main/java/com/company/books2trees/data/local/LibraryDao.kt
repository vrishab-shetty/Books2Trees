package com.company.books2trees.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.company.books2trees.data.model.LibraryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {

    @Query("SELECT * FROM library")
    fun getAll(): Flow<List<LibraryItem>>

    @Query("SELECT * FROM library where id = :id")
    suspend fun get(id: String): LibraryItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LibraryItem)

    @Query("DELETE from library where id = :id")
    suspend fun delete(id: String)

    @Update
    suspend fun update(entity: LibraryItem)
}