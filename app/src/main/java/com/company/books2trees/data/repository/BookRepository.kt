package com.company.books2trees.data.repository

import android.content.Context
import android.util.LruCache
import com.company.books2trees.data.local.BookDatabase
import com.company.books2trees.data.model.RecentItem
import com.company.books2trees.data.remote.BookFetcher
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.utils.UIHelper
import com.company.books2trees.presentation.utils.UIHelper.AWARDED_BOOKS_POSITION
import com.company.books2trees.presentation.utils.UIHelper.POPULAR_BOOKS_POSITION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class BookRepository(private val context: Context) {

    companion object {
        const val DEFAULT_GENRE = "All"
        private val searchCache = LruCache<Pair<String, String>, List<BookModel>>(5)

        fun getGenres(): List<String> {
            return listOf(
                "All", "Art", "Biography", "Business", "Children", "Comics",
                "Contemporary", "Cookbooks", "Crime", "Fantasy", "Fiction", "History",
                "Horror", "Comedy", "Music", "Mystery", "Nonfiction", "Philosophy",
                "Poetry", "Romance", "Science Fiction", "Self Help", "Sports",
                "Suspense", "Thriller"
            )
        }

        suspend fun search(query: String, filter: String?): List<BookModel> =
            withContext(Dispatchers.IO) {
                var results: List<BookModel>?
                synchronized(searchCache) {
                    results = searchCache.get(Pair(query, filter ?: DEFAULT_GENRE))
                }
                results ?: BookFetcher.searchBook(query, filter)
                    .also { searchCache.put(Pair(query, filter ?: DEFAULT_GENRE), it) }
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

    private suspend fun fetchItems() = withContext(Dispatchers.IO) {
        val popularItems = fetchPopularBooks()
        val awardedItems = fetchAwardedBooks()

        val result: MutableMap<Int, List<BookModel>> = mutableMapOf()
        result[UIHelper.getHomeListNames()[POPULAR_BOOKS_POSITION]] = popularItems
        result[UIHelper.getHomeListNames()[AWARDED_BOOKS_POSITION]] = awardedItems
        result
    }

    private fun fetchPopularBooks() = BookFetcher.fetchTrendingBooks()

    /**
     *  You can easily swap "pulitzer_prize" with other famous awards:
     *
         * national_book_award: For winners of the U.S. National Book Award.
         * booker_prize: For winners of the prestigious Booker Prize for fiction.
         * hugo_award: If you want to feature award-winning Science Fiction.
         * newbery_medal: For acclaimed works of children's literature.
     *
     */
    private fun fetchAwardedBooks(): List<BookModel> {
        return listOf(
            BookFetcher.fetchBooksBySubject("national_book_award", limit = 5),
            BookFetcher.fetchBooksBySubject("booker_prize", limit = 5),
            BookFetcher.fetchBooksBySubject("newbery_medal", limit = 5)
        ).flatten()
    }

    suspend fun insertRecent(model: BookModel) = withContext(Dispatchers.IO) {
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
}