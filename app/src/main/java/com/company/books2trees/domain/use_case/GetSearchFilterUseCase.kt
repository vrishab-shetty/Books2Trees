package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetSearchFilterUseCase(private val bookRepository: BookRepository) {
    operator fun invoke(): Flow<String> = bookRepository.getSearchFilterFlow()
}