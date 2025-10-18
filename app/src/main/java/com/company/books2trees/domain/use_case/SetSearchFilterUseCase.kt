package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.UserPreferencesRepository

class SetSearchFilterUseCase(private val userPreferencesRepository: UserPreferencesRepository) {
    suspend operator fun invoke(filter: String) = userPreferencesRepository.setSearchFilter(filter)
}