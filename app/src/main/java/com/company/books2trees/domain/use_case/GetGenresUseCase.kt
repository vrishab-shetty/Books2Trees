package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.BookRepository

class GetGenresUseCase(private val bookRepository: BookRepository) {
    suspend operator fun invoke(): List<String> = bookRepository.getGenres()
}