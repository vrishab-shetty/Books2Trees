package com.company.books2trees.data.repository

import android.content.Context
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.profile.LibraryPageItem
import com.company.books2trees.data.local.BookDatabase
import com.company.books2trees.data.model.LibraryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LibraryRepository(private val context: Context) {

    suspend fun insert(model: BookModel, categoryId: LibraryPageItem.CategoryId) =
        withContext(Dispatchers.IO) {
            val db = BookDatabase[context]
            val entity = LibraryItem(
                id = model.id,
                title = model.name,
                imgUrl = model.cover,
                url = model.url,
                categoryId = categoryId
            )



            db.libraryDao().insert(entity)
        }

    suspend fun delete(id: String) = withContext(Dispatchers.IO) {

        val db = BookDatabase[context]
        db.libraryDao().delete(id)

    }

    suspend fun update(item: LibraryItem) = withContext(Dispatchers.IO) {
        val db = BookDatabase[context]
        db.libraryDao().update(item)
    }

    fun load(): Flow<List<LibraryItem>> {
        val db = BookDatabase[context]

        return db.libraryDao().getAll()

    }


}