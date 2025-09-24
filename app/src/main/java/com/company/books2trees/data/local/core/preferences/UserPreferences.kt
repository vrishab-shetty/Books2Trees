package com.company.books2trees.data.local.core.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferences {

    /**
     * Saves the user's preferred search filter (genre).
     */
    suspend fun setSearchFilter(genre: String)

    /**
     * Gets a reactive stream of the user's preferred search filter.
     * The Flow will emit a new value whenever the filter changes.
     */
    fun getSearchFilter(): Flow<String?>
}