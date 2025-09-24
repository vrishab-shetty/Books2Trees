package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.BookRepository
import com.company.books2trees.presentation.profile.LibraryPageItem

class InsertBookToLibraryUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(model: BookModel, categoryId: LibraryPageItem.CategoryId) {
        bookRepository.insertLibraryBook(model, categoryId)
    }
}