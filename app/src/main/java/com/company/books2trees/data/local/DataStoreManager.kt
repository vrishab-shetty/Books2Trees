package com.company.books2trees.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    private val Context.searchDataStore by preferencesDataStore(name = "search_filter_preference")

    private object SearchPreferenceKeys {
        val GENRE = stringPreferencesKey("genre")
    }

    suspend fun setSearchFilter(genre: String) {
        context.searchDataStore.edit { preference ->
            preference[SearchPreferenceKeys.GENRE] = genre
        }
    }

    // ToDo DataStoreManager shouldn't depend on BookRepositoryImpl
    fun getSearchFilter(): Flow<String?> {
        return context.searchDataStore.data.map { preference ->
            preference[SearchPreferenceKeys.GENRE]
        }
    }
}