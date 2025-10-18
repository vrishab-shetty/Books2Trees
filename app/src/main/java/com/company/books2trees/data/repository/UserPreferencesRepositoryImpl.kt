package com.company.books2trees.data.repository

import com.company.books2trees.data.local.preferences.UserPreferences
import com.company.books2trees.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(private val userPreferences: UserPreferences) : UserPreferencesRepository {

    private val defaultGenre = "All"

    override fun getSearchFilter() = userPreferences.getSearchFilter().map { savedFilter ->
        savedFilter ?: defaultGenre
    }

    override suspend fun setSearchFilter(filter: String) = userPreferences.setSearchFilter(filter)


    override suspend fun getGenres() = listOf(
        "All", "Art", "Biography", "Business", "Children", "Comics",
        "Contemporary", "Cookbooks", "Crime", "Fantasy", "Fiction", "History",
        "Horror", "Comedy", "Music", "Mystery", "Nonfiction", "Philosophy",
        "Poetry", "Romance", "Science Fiction", "Self Help", "Sports",
        "Suspense", "Thriller"
    )
}