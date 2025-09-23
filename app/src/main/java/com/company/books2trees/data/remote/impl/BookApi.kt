package com.company.books2trees.data.remote.impl

import com.company.books2trees.data.remote.api.BookApi
import com.company.books2trees.data.remote.dto.common.BookDetailDto
import com.company.books2trees.data.remote.dto.common.SubjectResultDto
import com.company.books2trees.data.remote.dto.search.SearchResultDto
import com.company.books2trees.data.remote.dto.trending.TrendingBooksDto
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Locale

class BookApiImpl(
    private val client: OkHttpClient,
    private val gson: Gson
) : BookApi {

    private val openLibraryApiUrl = "https://openlibrary.org"

    override suspend fun fetchTrendingBooks(): TrendingBooksDto? {
        val url = "$openLibraryApiUrl/trending/weekly.json?limit=20"
        return fetchAndParse(url)
    }

    override suspend fun fetchBooksBySubject(subject: String, limit: Int): SubjectResultDto? {
        val formattedSubject = subject.trim().replace(" ", "_").lowercase(Locale.getDefault())
        val url = "$openLibraryApiUrl/subjects/$formattedSubject.json?limit=$limit"
        return fetchAndParse(url)
    }

    override suspend fun searchBooks(query: String, filter: String?): SearchResultDto? {
        val formattedQuery = query.trim().split("\\s+".toRegex()).joinToString("+")
        val fields = "key,title,author_name,cover_i,first_publish_year"

        val urlBuilder = "$openLibraryApiUrl/search.json?q=$formattedQuery&fields=$fields"
            .toHttpUrlOrNull()
            ?.newBuilder()
            ?: return null

        if (filter != null) {
            urlBuilder.addQueryParameter("subject", filter.lowercase(Locale.getDefault()))
        }

        return fetchAndParse(urlBuilder.build().toString())
    }

    override suspend fun fetchBookById(workId: String): BookDetailDto? {
        val url = "$openLibraryApiUrl$workId.json"
        return fetchAndParse(url)
    }

    /**
     * Generic function to execute a network request and parse the JSON response
     * into a given DTO class using Gson.
     * @param T The data class type to parse the JSON into.
     * @param url The URL to fetch.
     * @return A parsed DTO object of type T, or null if an error occurs.
     */
    private suspend inline fun <reified T> fetchAndParse(url: String): T? {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute() // This is a blocking call
                if (!response.isSuccessful) {
                    // In a real app, you'd want to log this error.
                    return@withContext null
                }
                val jsonString = response.body?.string()
                gson.fromJson(jsonString, T::class.java)
            } catch (e: Exception) {
                // Log the exception
                null
            }
        }
    }
}