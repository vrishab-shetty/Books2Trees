package com.company.books2trees.domain.use_case

import com.company.books2trees.data.local.library.model.LibraryItem
import com.company.books2trees.domain.repository.LibraryRepository

class UpdateLibraryItemUseCase(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(item: LibraryItem) {
        libraryRepository.updateLibraryBook(item)
    }
}