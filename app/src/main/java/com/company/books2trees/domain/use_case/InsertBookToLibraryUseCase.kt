package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.LibraryRepository
import com.company.books2trees.presentation.profile.LibraryPageItem

class InsertBookToLibraryUseCase(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(model: BookModel, categoryId: LibraryPageItem.CategoryId) {
        libraryRepository.insertLibraryBook(model, categoryId)
    }
}