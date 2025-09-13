package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetRecentBooksUseCase(
    private val bookRepository: BookRepository
) {
    operator fun invoke(): Flow<List<BookModel>> {
        return bookRepository.getRecentBooksFlow()
    }
}