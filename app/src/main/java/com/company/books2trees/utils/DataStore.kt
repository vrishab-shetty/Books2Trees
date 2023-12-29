package com.company.books2trees.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.company.books2trees.repos.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStore {

    private const val SEARCH_FILTER = "search_filter_preference"
    private val Context.searchDataStore by preferencesDataStore(
        name = SEARCH_FILTER
    )

    private object SearchPreferenceKeys {
        val GENRE = stringPreferencesKey("genre")
    }

    suspend fun setSearchFilter(context: Context, genre: String){
        context.searchDataStore.edit { preference ->
            preference[SearchPreferenceKeys.GENRE] = genre
        }
    }

    fun getSearchFilter(context: Context): Flow<String> = context.searchDataStore.data.map {preference ->
        preference[SearchPreferenceKeys.GENRE] ?: BookRepository.DEFAULT_GENRE
    }
}