package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.model.CategoryId
import com.company.books2trees.domain.model.LibraryBookModel
import com.company.books2trees.domain.repository.LibraryRepository

class AddBookToLibraryUseCase(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(model: BookModel, categoryId: CategoryId) {
        val libraryBook = LibraryBookModel(
            id = model.id,
            name = model.name,
            cover = model.cover,
            url = model.url,
            categoryId = categoryId
        )

        libraryRepository.addLibraryBook(libraryBook)
    }
}