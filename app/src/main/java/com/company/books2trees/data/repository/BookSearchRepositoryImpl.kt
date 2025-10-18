package com.company.books2trees.data.repository

import android.util.LruCache
import com.company.books2trees.data.mapper.toBookModel
import com.company.books2trees.data.remote.datasource.api.BookRemoteDataSource
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.BookSearchRepository

// Single source of Truth
class BookSearchRepositoryImpl(
    private val remoteDataSource: BookRemoteDataSource
) : BookSearchRepository {

    private val defaultGenre = "All"

    private val searchCache = LruCache<Pair<String, String>, List<BookModel>>(5)

    override suspend fun getTrendingBooks(): List<BookModel> {
        val trendingBooksDto = remoteDataSource.fetchTrendingBooks()
        return trendingBooksDto?.works?.map { it.toBookModel() } ?: emptyList()
    }

    override suspend fun getBooksBySubject(
        subject: String,
        limit: Int
    ): List<BookModel> {
        val subjectResultDto = remoteDataSource.fetchBooksBySubject(subject, limit)
        return subjectResultDto?.works?.map { it.toBookModel() } ?: emptyList()
    }

    override suspend fun searchBooks(query: String, filter: String): List<BookModel> {
            val cacheKey = Pair(query, filter)

        return searchCache.get(cacheKey) ?: run {
                val searchResultDto = remoteDataSource.searchBooks(
                    query, if (filter == defaultGenre) null else filter
                )
                val displayModels =
                    searchResultDto?.searchResults?.map { it.toBookModel() } ?: emptyList()

                searchCache.put(cacheKey, displayModels)
                displayModels
            }
    }
}