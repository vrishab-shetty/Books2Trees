package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.LibraryRepository

class RemoveLibraryBookUseCase(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(id: String) {
        libraryRepository.removeLibraryBook(id)
    }
}