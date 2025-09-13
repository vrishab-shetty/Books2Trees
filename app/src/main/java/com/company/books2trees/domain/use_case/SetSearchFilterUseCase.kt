package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.BookRepository

class SetSearchFilterUseCase(private val bookRepository: BookRepository) {
    suspend operator fun invoke(filter: String) = bookRepository.setSearchFilter(filter)
}