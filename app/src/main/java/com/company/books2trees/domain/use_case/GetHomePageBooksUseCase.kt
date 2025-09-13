package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.BookRepository
import com.company.books2trees.presentation.utils.UIHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GetHomePageBooksUseCase(private val bookRepository: BookRepository) {

    suspend operator fun invoke(): Map<Int, List<BookModel>> = withContext(Dispatchers.IO) {
        // Fetch popular and awarded books in parallel for efficiency
        val popularItemsDeferred = async { bookRepository.getTrendingBooks() }
        val awardedItemsDeferred = async { fetchAwardedBooks() }

        val popularItems = popularItemsDeferred.await()
        val awardedItems = awardedItemsDeferred.await()

        // This is the business logic for constructing the home page map
        mutableMapOf(
            UIHelper.getHomeListNames()[UIHelper.POPULAR_BOOKS_POSITION] to popularItems,
            UIHelper.getHomeListNames()[UIHelper.AWARDED_BOOKS_POSITION] to awardedItems
        )
    }

    /**
     *  You can easily swap with other famous awards:
     * "pulitzer_prize"
     * national_book_award: For winners of the U.S. National Book Award.
     * booker_prize: For winners of the prestigious Booker Prize for fiction.
     * hugo_award: If you want to feature award-winning Science Fiction.
     * newbery_medal: For acclaimed works of children's literature.
     *
     */
    private suspend fun fetchAwardedBooks(): List<BookModel> {
        val subjects = listOf("national_book_award", "booker_prize", "newbery_medal")
        return subjects.flatMap { subject ->
            bookRepository.getBooksBySubject(subject, limit = 5)
        }
    }
}