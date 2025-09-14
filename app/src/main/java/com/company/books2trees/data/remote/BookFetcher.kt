package com.company.books2trees.data.remote

import com.company.books2trees.data.remote.dto.BookDetailDto
import com.company.books2trees.data.remote.dto.SearchResultDto
import com.company.books2trees.data.remote.dto.SubjectResultDto
import com.company.books2trees.data.remote.dto.TrendingBooksDto
import com.google.gson.Gson
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.Locale

object BookFetcher {

    private const val OPEN_LIBRARY_API_URL = "https://openlibrary.org"

    private val client = OkHttpClient()
    private val gson = Gson()

    fun fetchTrendingBooks(): TrendingBooksDto? {
        val url = "$OPEN_LIBRARY_API_URL/trending/weekly.json?limit=20"
        return fetchAndParse(url)
    }

    fun fetchBooksBySubject(subject: String, limit: Int = 20): SubjectResultDto? {
        val formattedSubject = subject.trim().replace(" ", "_").lowercase(Locale.getDefault())
        val url = "$OPEN_LIBRARY_API_URL/subjects/$formattedSubject.json?limit=$limit"
        return fetchAndParse(url)
    }

    fun searchBook(query: String, filter: String? = null): SearchResultDto? {
        val formattedQuery = query.trim().split("\\s+".toRegex()).joinToString("+")
        val fields = "key,title,author_name,cover_i,first_publish_year"

        val urlBuilder = "$OPEN_LIBRARY_API_URL/search.json?q=$formattedQuery&fields=$fields"
            .toHttpUrlOrNull()
            ?.newBuilder()
            ?: return null // Return null if the base URL is invalid

        if (filter != null) {
            urlBuilder.addQueryParameter("subject", filter.lowercase(Locale.getDefault()))
        }

        return fetchAndParse(urlBuilder.build().toString())
    }

    fun fetchBookById(workId: String): BookDetailDto? {
        val url = "$OPEN_LIBRARY_API_URL$workId.json"
        return fetchAndParse(url)
    }

    /**
     * Generic function to execute a network request and parse the JSON response
     * into a given DTO class using Gson.
     * @param T The data class type to parse the JSON into.
     * @param url The URL to fetch.
     * @return A parsed DTO object of type T, or null if an error occurs.
     */
    private inline fun <reified T> fetchAndParse(url: String): T? {
        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException("Unsuccessful response code: ${response.code}")
            }
            val jsonString = response.body.string()
            return gson.fromJson(jsonString, T::class.java)
        } catch (_: Exception) {
            return null
        }
    }

}