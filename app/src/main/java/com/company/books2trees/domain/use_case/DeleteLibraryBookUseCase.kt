package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.BookRepository

class DeleteLibraryBookUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(id: String) {
        bookRepository.deleteLibraryBook(id)
    }
}