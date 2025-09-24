package com.company.books2trees.data.local.book.impl

import android.util.LruCache
import com.company.books2trees.data.local.core.preferences.UserPreferences
import com.company.books2trees.data.local.recent.BookLocalDataSource
import com.company.books2trees.data.local.recent.mapper.toBookModel
import com.company.books2trees.data.local.recent.mapper.toRecentItemEntity
import com.company.books2trees.data.remote.api.BookApi
import com.company.books2trees.data.remote.dto.common.mapper.toBookModel
import com.company.books2trees.data.remote.dto.search.mapper.toBookModel
import com.company.books2trees.data.remote.dto.trending.mapper.toBookModel
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// Single source of Truth
class BookRepositoryImpl(
    private val remoteDataSource: BookApi,
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

    override suspend fun addRecentBook(model: BookModel) = withContext(Dispatchers.IO) {
        val entity = model.toRecentItemEntity()
        localDataSource.insertRecentItem(entity)
    }

    override suspend fun removeRecentBook(id: String) = withContext(Dispatchers.IO) {
        localDataSource.deleteRecentItem(id)
    }

    override fun getRecentBooksFlow(): Flow<List<BookModel>> {
        return localDataSource.getAllRecentItems().map { recentItems ->
            recentItems.map {
                it.toBookModel()
            }
        }
    }

    override fun getSearchFilterFlow(): Flow<String> {
        return userPreferences.getSearchFilter().map { savedFilter ->
            savedFilter ?: DEFAULT_GENRE
        }
    }

    override suspend fun setSearchFilter(filter: String) {
        userPreferences.setSearchFilter(filter)
    }

    override suspend fun getGenres(): List<String> {
        return listOf(
            "All", "Art", "Biography", "Business", "Children", "Comics",
            "Contemporary", "Cookbooks", "Crime", "Fantasy", "Fiction", "History",
            "Horror", "Comedy", "Music", "Mystery", "Nonfiction", "Philosophy",
            "Poetry", "Romance", "Science Fiction", "Self Help", "Sports",
            "Suspense", "Thriller"
        )
    }
}