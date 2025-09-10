package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.BookRepository

class AddRecentBookUseCase(private val bookRepository: BookRepository) {
    suspend operator fun invoke(book: BookModel) {
        bookRepository.addRecentBook(book)
    }
}