package com.company.books2trees.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getSearchFilter(): Flow<String>
    suspend fun setSearchFilter(filter: String)
    suspend fun getGenres(): List<String>
}