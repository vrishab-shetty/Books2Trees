package com.company.books2trees.domain.use_case

import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.domain.repository.BookRepository

class UpdateLibraryItemUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(item: LibraryBookEntity) {
        bookRepository.updateLibraryBook(item)
    }
}