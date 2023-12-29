package com.company.books2trees.repos

import android.content.Context
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.profile.LibraryPageItem
import com.company.books2trees.ui.profile.database.BookDatabase
import com.company.books2trees.ui.profile.database.LibraryItem
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