package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetSearchFilterUseCase(private val userPreferencesRepository: UserPreferencesRepository) {
    operator fun invoke(): Flow<String> = userPreferencesRepository.getSearchFilter()
}