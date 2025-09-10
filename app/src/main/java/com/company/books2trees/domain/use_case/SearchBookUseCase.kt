package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.BookRepository

class SearchBooksUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(query: String, filter: String?): List<BookModel> {
        // Business rule: Don't hit the network or cache if the query is empty.
        if (query.isBlank()) {
            return emptyList()
        }
        return bookRepository.searchBooks(query, filter)
    }
}