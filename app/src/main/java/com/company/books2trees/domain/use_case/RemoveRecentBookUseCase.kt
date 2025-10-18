package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.RecentBooksRepository

class RemoveRecentBookUseCase(private val recentBooksRepository: RecentBooksRepository) {
    suspend operator fun invoke(id: String) {
        recentBooksRepository.removeRecentBook(id)
    }
}