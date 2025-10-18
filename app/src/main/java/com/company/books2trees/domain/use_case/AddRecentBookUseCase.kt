package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.RecentBooksRepository

class AddRecentBookUseCase(private val recentBooksRepository: RecentBooksRepository) {
    suspend operator fun invoke(book: BookModel) {
        recentBooksRepository.addRecentBook(book)
    }
}