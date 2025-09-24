package com.company.books2trees.data.remote.api

import com.company.books2trees.data.remote.dto.common.BookDetailDto
import com.company.books2trees.data.remote.dto.common.SubjectResultDto
import com.company.books2trees.data.remote.dto.search.SearchResultDto
import com.company.books2trees.data.remote.dto.trending.TrendingBooksDto

interface BookApi {

    /**
     * Fetches a list of books that are currently trending.
     */
    suspend fun fetchTrendingBooks(): TrendingBooksDto?

    /**
     * Fetches a list of books for a given subject.
     * @param subject The subject to search for (e.g., "science_fiction").
     * @param limit The maximum number of results to return.
     */
    suspend fun fetchBooksBySubject(subject: String, limit: Int = 20): SubjectResultDto?

    /**
     * Searches for books matching a query.
     * @param query The search term.
     * @param filter An optional subject to filter the search results by.
     */
    suspend fun searchBooks(query: String, filter: String? = null): SearchResultDto?

    /**
     * Fetches the detailed information for a specific book by its ID.
     * @param workId The unique identifier for the book.
     */
    suspend fun fetchBookById(workId: String): BookDetailDto?
}
