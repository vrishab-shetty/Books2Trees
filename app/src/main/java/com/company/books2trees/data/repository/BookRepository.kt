package com.company.books2trees.data.repository

import android.content.Context
import android.util.LruCache
import com.company.books2trees.presentation.utils.awardedItems
import com.company.books2trees.presentation.utils.popularItems
import com.company.books2trees.presentation.utils.searchResult
import com.company.books2trees.data.model.RecentItem
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.data.local.BookDatabase
import com.company.books2trees.presentation.utils.UIHelper
import com.company.books2trees.presentation.utils.UIHelper.AWARDED_BOOKS_POSITION
import com.company.books2trees.presentation.utils.UIHelper.POPULAR_BOOKS_POSITION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class BookRepository(private val context: Context) {

    enum class GenreList {
        All,
        Art,
        Biography,
        Business,
        ChickLit,
        Children,
        Comics,
        Contemporary,
        Cookbooks,
        Crime,
        Fantasy,
        Fiction,
        Graphic,
        History,
        Horror,
        Comedy,
        Music,
        Mystery,
        Nonfiction,
        Philosophy,
        Poetry,
        Romance,
        ScienceFiction,
        SelfHelp,
        Suspense,
        Sports,
        Thriller;

        companion object {
            fun toList() = entries.map { it.name }

        }

    }

    fun fetchItemsFlow(): Flow<Result<Map<Int, List<BookModel>>>> = flow {
        try {
            val items = fetchItems()
            emit(Result.success(items))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    //     Calls the required requests
    private suspend fun fetchItems() = withContext(
        Dispatchers.IO
    ) {

        delay(1000)

        val popularItems =
            popularItems
//            BookFetcher.fetchPopularItems()

        val awardedItems =
            awardedItems
//            BookFetcher.fetchAwardedItems()

        val result: MutableMap<Int, List<BookModel>> = mutableMapOf()

        result[UIHelper.getHomeListNames()[POPULAR_BOOKS_POSITION]] = popularItems
        result[UIHelper.getHomeListNames()[AWARDED_BOOKS_POSITION]] = awardedItems

        result
    }

    suspend fun insertRecent(model: BookModel) = withContext(
        Dispatchers.IO
    ) {
        val db = BookDatabase[context]
        val entity = RecentItem()

        entity.id = model.id
        entity.extras = model.extras
        entity.imgUrl = model.cover
        entity.title = model.name
        entity.url = model.url

        db.recentDao().insert(entity)
    }

    suspend fun deleteRecent(id: String) = withContext(Dispatchers.IO) {

        val db = BookDatabase[context]
        db.recentDao().delete(id)

    }

    fun loadRecent(): Flow<List<RecentItem>> {
        val db = BookDatabase[context]

        return db.recentDao().getAll()

    }

    companion object {
        val DEFAULT_GENRE = GenreList.All.name
        private val searchCache = LruCache<Pair<String, String>, List<BookModel>>(5)

        suspend fun search(query: String, filter: String?): List<BookModel> =
            withContext(Dispatchers.IO) {
                delay(1000)
                searchResult

//                var results: List<BookModel>?
//
//                synchronized(searchCache) {
//                    results = searchCache.get(Pair(query, filter ?: DEFAULT_GENRE))
//                }
//                results ?: BookFetcher.searchBook(query, filter)
//                    .also { searchCache.put(Pair(query, filter ?: DEFAULT_GENRE), it) }
            }


    }
}