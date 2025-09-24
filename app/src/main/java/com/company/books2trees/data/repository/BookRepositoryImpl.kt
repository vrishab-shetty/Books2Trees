package com.company.books2trees.data.repository

import android.util.LruCache
import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.data.local.datasource.api.BookLocalDataSource
import com.company.books2trees.data.local.preferences.UserPreferences
import com.company.books2trees.data.mapper.toBookModel
import com.company.books2trees.data.mapper.toBookModelFromRecent
import com.company.books2trees.data.mapper.toLibraryItemEntity
import com.company.books2trees.data.mapper.toRecentItemEntity
import com.company.books2trees.data.remote.datasource.api.BookRemoteDataSource
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.BookRepository
import com.company.books2trees.presentation.profile.LibraryPageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// Single source of Truth
class BookRepositoryImpl(
    private val remoteDataSource: BookRemoteDataSource,
    private val localDataSource: BookLocalDataSource,
    private val userPreferences: UserPreferences
) : BookRepository {

    private val DEFAULT_GENRE = "All"

    private val searchCache = LruCache<Pair<String, String>, List<BookModel>>(5)

    override suspend fun getTrendingBooks(): List<BookModel> = withContext(Dispatchers.IO) {
        val trendingBooksDto = remoteDataSource.fetchTrendingBooks()
        trendingBooksDto?.works?.map { it.toBookModel() } ?: emptyList()
    }

    override suspend fun getBooksBySubject(
        subject: String,
        limit: Int
    ): List<BookModel> = withContext(Dispatchers.IO) {
        val subjectResultDto = remoteDataSource.fetchBooksBySubject(subject, limit)
        subjectResultDto?.works?.map { it.toBookModel() } ?: emptyList()
    }

    override suspend fun searchBooks(query: String, filter: String): List<BookModel> =
        withContext(Dispatchers.IO) {
            val cacheKey = Pair(query, filter)

            searchCache.get(cacheKey) ?: run {
                val searchResultDto = remoteDataSource.searchBooks(
                    query, if (filter == DEFAULT_GENRE) null else filter
                )
                val displayModels =
                    searchResultDto?.searchResults?.map { it.toBookModel() } ?: emptyList()

                searchCache.put(cacheKey, displayModels)
                displayModels
            }
        }

    override suspend fun addRecentBook(model: BookModel) {
        val entity = model.toRecentItemEntity()
        localDataSource.insertRecentItem(entity)
    }

    override suspend fun removeRecentBook(id: String) = localDataSource.deleteRecentItem(id)

    override fun getRecentBooksFlow() = localDataSource.getAllRecentItems().map { recentItems ->
        recentItems.map {
            it.toBookModelFromRecent()
        }
    }

    override fun getSearchFilterFlow() = userPreferences.getSearchFilter().map { savedFilter ->
        savedFilter ?: DEFAULT_GENRE
    }

    override suspend fun setSearchFilter(filter: String) = userPreferences.setSearchFilter(filter)


    override suspend fun getGenres() = listOf(
        "All", "Art", "Biography", "Business", "Children", "Comics",
        "Contemporary", "Cookbooks", "Crime", "Fantasy", "Fiction", "History",
        "Horror", "Comedy", "Music", "Mystery", "Nonfiction", "Philosophy",
        "Poetry", "Romance", "Science Fiction", "Self Help", "Sports",
        "Suspense", "Thriller"
    )

    override fun getAllLibraryBooksFlow() = localDataSource.getAllLibraryItems()

    override suspend fun insertLibraryBook(
        model: BookModel, categoryId: LibraryPageItem.CategoryId
    ) {
        val entity = model.toLibraryItemEntity(categoryId)
        localDataSource.insertLibraryItem(entity)
    }

    override suspend fun deleteLibraryBook(id: String) = localDataSource.deleteLibraryItem(id)

    override suspend fun updateLibraryBook(item: LibraryBookEntity) =
        localDataSource.updateLibraryItem(item)
}