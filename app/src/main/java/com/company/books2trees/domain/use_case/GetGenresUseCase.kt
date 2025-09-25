package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.UserPreferencesRepository

class GetGenresUseCase(private val userPreferencesRepository: UserPreferencesRepository) {
    suspend operator fun invoke(): List<String> = userPreferencesRepository.getGenres()
}