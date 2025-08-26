package com.company.books2trees.repos

import android.util.Log
import com.company.books2trees.ui.models.AwardedBookModel
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.models.PopularBookModel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.Locale


object BookFetcher {
    
    enum class BookExtrasType {
        AUTHORS, PAGES, NONE
    }
    private const val API_KEY = "c0b850ea78mshc0c881d0496b98bp11f98fjsn498d1c65d248"
    private const val API_HOST = "hapi-books.p.rapidapi.com"
    private const val TAG = "BookFetcher"

    private val client = OkHttpClient()

    fun fetchPopularItems(): List<PopularBookModel> {

        val items = mutableListOf<PopularBookModel>()

        try {
            val request = Request.Builder()
                .url("https://hapi-books.p.rapidapi.com/month/2022/3")
                .get()
                .addHeader(
                    "X-RapidAPI-Key", API_KEY
                )
                .addHeader(
                    "X-RapidAPI-Host", API_HOST
                )
                .build()

            val response = client.newCall(request).execute()
            response.body?.string()?.let { jsonString ->
                Log.i(TAG, jsonString)
                if(response.isSuccessful)
                    parsePopularItems(items, jsonString)
                else throw IOException(jsonString)
            }
        } catch (err: IOException) {
            Log.e(TAG, "Failed to fetch popular items", err)
        }

        return items
    }

    private fun parsePopularItems(
        items: MutableList<PopularBookModel>,
        jsonString: String
    ) {

        val bookArray = JSONArray(jsonString)

        for (i in 0 until bookArray.length()) {
            val bookJSONObject: JSONObject = bookArray.getJSONObject(i)
            val item = PopularBookModel(
                bookJSONObject.getString("book_id"),
                bookJSONObject.getInt("position"),
                bookJSONObject.getString("name"),
                bookJSONObject.getDouble("rating"),
                bookJSONObject.getString("cover"),
                bookJSONObject.getString("url")
            )

            items.add(item)
        }

    }

    fun searchBook(query: String, filter: String? = null): List<BookModel> {

        val items = mutableListOf<BookModel>()
        val values = query.split("\\s").joinToString("+")
        if (filter == null || filter == BookRepository.DEFAULT_GENRE) {
            try {
                val request = Request.Builder()
                    .url("https://hapi-books.p.rapidapi.com/search/$values")
                    .addHeader(
                        "X-RapidAPI-Key", API_KEY
                    )
                    .addHeader(
                        "X-RapidAPI-Host", API_HOST
                    )
                    .build()

                val response = client.newCall(request).execute()
                response.body?.string()?.let { jsonString ->
                    Log.i(TAG, jsonString)
                    if (response.isSuccessful)
                        parseItems(items, jsonString, BookExtrasType.AUTHORS)
                    else throw IOException(jsonString)
                }
            } catch (err: IOException) {
                Log.e(TAG, "Failed on the search query", err)
            }
        } else {
            try {
                val request = Request.Builder()
                    .url("https://hapi-books.p.rapidapi.com/week/${filter.lowercase(Locale.getDefault())}/10")
                    .addHeader(
                        "X-RapidAPI-Key", API_KEY
                    )
                    .addHeader(
                        "X-RapidAPI-Host", API_HOST
                    )
                    .build()

                val response = client.newCall(request).execute()
                response.body?.string()?.let { jsonString ->
                    Log.i(TAG, jsonString)
                    if (response.isSuccessful)
                        parseItems(items, jsonString, BookExtrasType.NONE)
                    else throw IOException(jsonString)
                }
            } catch (err: IOException) {
                Log.e(TAG, "Failed on the search query with filter", err)
            }
        }

        return items.filter { book ->
            var match = false
            query.split("\\s").forEach {
                match = match || book.name.contains(it, true)
            }
            match
        }
    }

    private fun parseItems(
        items: MutableList<BookModel>,
        jsonString: String,
        extras: BookExtrasType
    ) {
        val bookArray = JSONArray(jsonString)

        for (i in 0 until bookArray.length()) {
            val bookJSONObject: JSONObject = bookArray.getJSONObject(i)
            val extrasString: String? =
                when (extras) {
                    BookExtrasType.AUTHORS -> {
                        val authorJSONArray: JSONArray = bookJSONObject.getJSONArray("authors")
                        authorJSONArray.join(", ")
                    }

                    BookExtrasType.PAGES -> {
                        bookJSONObject.getString("pages")
                    }

                    else -> null
                }
            val item = BookModel(
                bookJSONObject.getString("book_id"),
                bookJSONObject.getString("name"),
                bookJSONObject.getString("cover"),
                bookJSONObject.getString("url"),
                extrasString
            )

            items.add(item)
        }

    }

    fun fetchBookById(modelId: String): BookModel? =
        try {

            val request = Request.Builder()
                .url("https://hapi-books.p.rapidapi.com/book/$modelId")
                .get()
                .addHeader(
                    "X-RapidAPI-Key", API_KEY
                )
                .addHeader(
                    "X-RapidAPI-Host", API_HOST
                )
                .build()

            val response = client.newCall(request).execute()
            response.body?.string()?.let { jsonString ->
                Log.i(TAG, jsonString)
                if (response.isSuccessful)
                    parseItem(jsonString, BookExtrasType.PAGES)
                else throw IOException(jsonString)
            }
        } catch (err: IOException) {
            Log.e(TAG, "Failed to fetch item with ID: $modelId", err)
            null
        }


    private fun parseItem(jsonString: String, extras: BookExtrasType): BookModel {

        val bookJSONObject = JSONObject(jsonString)
        val extrasString: String? =
            when (extras) {
                BookExtrasType.AUTHORS -> {
                    val authorJSONArray: JSONArray = bookJSONObject.getJSONArray("authors")
                    authorJSONArray.join(", ")
                }

                BookExtrasType.PAGES -> {
                    bookJSONObject.getString("pages")
                }

                else -> null
            }
        return BookModel(
            bookJSONObject.getString("book_id"),
            bookJSONObject.getString("name"),
            bookJSONObject.getString("cover"),
            bookJSONObject.getString("url"),
            extrasString
        )
    }


    fun fetchAwardedItems(): List<AwardedBookModel> {

        val items = mutableListOf<AwardedBookModel>()


        try {
            val request = Request.Builder()
                .url("https://hapi-books.p.rapidapi.com/top/2022")
                .get()
                .addHeader(
                    "X-RapidAPI-Key", API_KEY
                )
                .addHeader(
                    "X-RapidAPI-Host", API_HOST
                )
                .build()

            val response = client.newCall(request).execute()

            response.body?.string()?.let { jsonString ->
                if (response.isSuccessful)
                    parseAwardedItems(items, jsonString)
                else throw IOException(jsonString)
            }
        } catch (err: IOException) {
            Log.e(TAG, "Failed to fetch awarded items", err)
        }

        return items

    }

    private fun parseAwardedItems(items: MutableList<AwardedBookModel>, jsonString: String) {

        val bookArray = JSONArray(jsonString)

        for (i in 0 until bookArray.length()) {
            val bookJSONObject: JSONObject = bookArray.getJSONObject(i)
            val item = AwardedBookModel(
                bookJSONObject.getString("book_id"),
                bookJSONObject.getString("category"),
                bookJSONObject.getString("name"),
                bookJSONObject.getString("cover"),
                bookJSONObject.getString("url")
            )

            items.add(item)
        }
    }
}