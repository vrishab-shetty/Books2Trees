package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.BookRepository
import com.company.books2trees.presentation.profile.LibraryPageItem
import com.company.books2trees.presentation.profile.LibraryPageItem.CategoryId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A Use Case that loads all library items and groups them by category.
 * This encapsulates the business logic for structuring the profile screen content.
 */
class GetProfileContentUseCase(
    private val bookRepository: BookRepository
) {
    operator fun invoke(): Flow<List<LibraryPageItem>> {
        return bookRepository.getAllLibraryBooksFlow().map { models ->
            // The logic to group by category is a business rule.
            CategoryId.entries
                .associateWith { categoryId -> models.filter { it.categoryId == categoryId } }
                .map { LibraryPageItem(it.key, it.value) }
        }
    }
}