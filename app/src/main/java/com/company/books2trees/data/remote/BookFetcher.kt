package com.company.books2trees.data.remote

import android.util.Log
import com.company.books2trees.data.repository.BookRepository
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.model.SimpleBookModel
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

object BookFetcher {

    private const val TAG = "BookFetcher"
    private const val OPEN_LIBRARY_API_URL = "https://openlibrary.org"
    private const val OPEN_LIBRARY_COVERS_URL = "https://covers.openlibrary.org"

    private val client = OkHttpClient()

    fun fetchPopularItems(): List<BookModel> {
        return fetchBooksBySubject("love")
    }

    fun fetchAwardedItems(): List<BookModel> {
        return fetchBooksBySubject("history")
    }

    private fun fetchBooksBySubject(subject: String): List<BookModel> {
        val items = mutableListOf<BookModel>()
        val url = "$OPEN_LIBRARY_API_URL/subjects/$subject.json?limit=20&details=true" // Get up to 20 books

        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            response.body.string().let { jsonString ->
                Log.d(TAG, "SUBJECT RESPONSE ($subject): $jsonString")

                if (response.isSuccessful) {
                    val jsonResponse = JSONObject(jsonString)
                    // In the Subjects API, the results are in a "works" array
                    val worksArray = jsonResponse.getJSONArray("works")
                    parseBookDocs(items, worksArray) // We can reuse the search parser!
                } else throw IOException("Unsuccessful response: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch subject: $subject", e)
        }
        return items
    }

    fun searchBook(query: String, filter: String? = null): List<BookModel> {
        val items = mutableListOf<BookModel>()
        val formattedQuery = query.trim().split("\\s+".toRegex()).joinToString("+")

        val fields = "key,title,author_name,cover_i"

        val urlBuilder = "$OPEN_LIBRARY_API_URL/search.json?q=$formattedQuery&fields=$fields".toHttpUrlOrNull()?.newBuilder()
        if (filter != null && filter != BookRepository.DEFAULT_GENRE) {
            urlBuilder?.addQueryParameter("subject", filter.lowercase(Locale.getDefault()))
        }
        val url = urlBuilder?.build().toString()

        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            response.body.string().let { jsonString ->
                if (response.isSuccessful) {
                    val jsonResponse = JSONObject(jsonString)
                    val docsArray = jsonResponse.getJSONArray("docs")
                    parseBookDocs(items, docsArray)
                } else throw IOException("Unsuccessful response: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed on search query: $query", e)
        }
        return items
    }

    fun fetchBookById(modelId: String): BookModel? {
        val url = "$OPEN_LIBRARY_API_URL$modelId.json"
        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            return response.body?.string()?.let { jsonString ->
                if (response.isSuccessful) {
                    parseSingleBook(jsonString)
                } else {
                    throw IOException("Unsuccessful response: ${response.message}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch item with ID: $modelId", e)
            return null
        }
    }

    private fun parseSingleBook(jsonString: String): BookModel {
        val bookJson = JSONObject(jsonString)
        val title = bookJson.optString("title", "No Title")
        val id = bookJson.optString("key", "")

        val coversArray = bookJson.optJSONArray("covers")
        val coverUrl = if (coversArray != null && coversArray.length() > 0) {
            "$OPEN_LIBRARY_COVERS_URL/b/id/${coversArray.getInt(0)}-L.jpg"
        } else ""

        val descriptionObj = bookJson.optJSONObject("description")
        val description = descriptionObj?.optString("value") ?: bookJson.optString("description", "No description available.")

        return SimpleBookModel(
            id = id,
            name = title,
            cover = coverUrl,
            url = OPEN_LIBRARY_API_URL + id,
            extras = description
        )
    }

    private fun parseBookDocs(items: MutableList<BookModel>, docsArray: JSONArray) {
        for (i in 0 until docsArray.length()) {
            val bookJson = docsArray.getJSONObject(i)
            val title = bookJson.optString("title", "No Title")
            val id = bookJson.optString("key", "")

            val coverId = bookJson.optLong("cover_i", -1).let {
                if (it != -1L) it else bookJson.optLong("cover_id", -1)
            }
            val coverUrl = if (coverId != -1L) "$OPEN_LIBRARY_COVERS_URL/b/id/$coverId-L.jpg" else ""

            var authors = "Unknown Author"
            val authorsNameArray = bookJson.optJSONArray("author_name")
            if (authorsNameArray != null && authorsNameArray.length() > 0) {
                authors = (0 until authorsNameArray.length()).joinToString(", ") {
                    authorsNameArray.getString(it)
                }
            } else {
                // If not found, check for "authors" (a list of objects from Subjects)
                val authorsArray = bookJson.optJSONArray("authors")
                if (authorsArray != null && authorsArray.length() > 0) {
                    authors = (0 until authorsArray.length()).joinToString(", ") {
                        authorsArray.getJSONObject(it).optString("name", "")
                    }
                }
            }

            items.add(SimpleBookModel(
                id = id,
                name = title,
                cover = coverUrl,
                url = OPEN_LIBRARY_API_URL + id,
                extras = authors
            ))
        }
    }
}