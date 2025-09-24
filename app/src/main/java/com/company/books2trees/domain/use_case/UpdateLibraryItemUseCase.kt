package com.company.books2trees.domain.use_case

import com.company.books2trees.data.local.library.model.LibraryItem
import com.company.books2trees.domain.repository.BookRepository

class UpdateLibraryItemUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(item: LibraryItem) {
        bookRepository.updateLibraryBook(item)
    }
}