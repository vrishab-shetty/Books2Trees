package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.repository.RecentBooksRepository
import kotlinx.coroutines.flow.Flow

class GetRecentBooksUseCase(
    private val recentBooksRepository: RecentBooksRepository
) {
    operator fun invoke(): Flow<List<BookModel>> {
        return recentBooksRepository.getRecentBooks()
    }
}