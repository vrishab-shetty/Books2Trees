package com.company.books2trees.data.local.library.impl

import com.company.books2trees.data.local.library.api.LibraryDao
import com.company.books2trees.data.local.library.mapper.toLibraryItemEntity
import com.company.books2trees.data.local.library.model.LibraryItem
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.LibraryRepository
import com.company.books2trees.presentation.profile.LibraryPageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// ToDo: Change Category access and LibraryDoa
class LibraryRepositoryImpl(
    private val libraryDao: LibraryDao
) : LibraryRepository {

    override suspend fun insertLibraryBook(
        model: BookModel,
        categoryId: LibraryPageItem.CategoryId
    ) =
        withContext(Dispatchers.IO) {
            val entity = model.toLibraryItemEntity(categoryId)
            libraryDao.insert(entity)
        }

    override suspend fun deleteLibraryBook(id: String) = withContext(Dispatchers.IO) {
        libraryDao.delete(id)
    }

    override suspend fun updateLibraryBook(item: LibraryItem) = withContext(Dispatchers.IO) {
        libraryDao.update(item)
    }

    override fun getAllLibraryBooksFlow(): Flow<List<LibraryItem>> {
        return libraryDao.getAll()
    }


}