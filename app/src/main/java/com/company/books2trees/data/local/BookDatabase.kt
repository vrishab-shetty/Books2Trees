package com.company.books2trees.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.company.books2trees.data.local.model.LibraryItem
import com.company.books2trees.data.local.model.RecentItem

private const val DATABASE_NAME = "books2trees.db"

/**
 *  Note: If in manifest file, the variable allowBackup is set to true
 *  then uninstalling and reinstalling the app while not reset the database.
 *
 */
@Database(entities = [LibraryItem::class, RecentItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BookDatabase : RoomDatabase() {

    abstract fun libraryDao(): LibraryDao

    abstract fun recentDao(): RecentBookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        @Synchronized
        operator fun get(context: Context): BookDatabase {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context, BookDatabase::class.java, DATABASE_NAME)
                        .build()
            }

            return INSTANCE!!
        }


    }
}