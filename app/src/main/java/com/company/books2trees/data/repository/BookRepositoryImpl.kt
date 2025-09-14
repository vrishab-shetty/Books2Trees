package com.company.books2trees.data.repository

import android.util.LruCache
import com.company.books2trees.data.local.BookLocalDataSource
import com.company.books2trees.data.local.DataStoreManager
import com.company.books2trees.data.local.mapper.toBookModel
import com.company.books2trees.data.local.mapper.toRecentItemEntity
import com.company.books2trees.data.remote.BookFetcher
import com.company.books2trees.data.remote.mapper.toBookModel
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// Single source of Truth
class BookRepositoryImpl(
    private val remoteDataSource: BookFetcher,
    private val localDataSource: BookLocalDataSource,
    private val dataStoreManager: DataStoreManager
) : BookRepository {

    companion object {
        const val DEFAULT_GENRE = "All"
    }

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
                val searchResultDto = remoteDataSource.searchBook(
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
        return dataStoreManager.getSearchFilter().map { savedFilter ->
            savedFilter ?: DEFAULT_GENRE
        }
    }

    override suspend fun setSearchFilter(filter: String) {
        dataStoreManager.setSearchFilter(filter)
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