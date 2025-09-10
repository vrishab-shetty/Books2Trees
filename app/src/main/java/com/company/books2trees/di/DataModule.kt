package com.company.books2trees.di

import com.company.books2trees.data.local.BookDatabase
import com.company.books2trees.data.local.BookLocalDataSource
import com.company.books2trees.data.local.DataStoreManager
import com.company.books2trees.data.local.LibraryDao
import com.company.books2trees.data.local.RecentBookDao
import com.company.books2trees.data.remote.BookFetcher
import com.company.books2trees.data.repository.BookRepositoryImpl
import com.company.books2trees.data.repository.LibraryRepositoryImpl
import com.company.books2trees.domain.repository.BookRepository
import com.company.books2trees.domain.repository.LibraryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    // Database & DAOs
    single { BookDatabase[androidContext()] }
    single { get<BookDatabase>().libraryDao() }
    single { get<BookDatabase>().recentDao() }

    // Data Source
    single { BookFetcher }
    single { DataStoreManager(androidContext()) }
    single { BookLocalDataSource(get<RecentBookDao>()) }

    // Repositories
    single<BookRepository> {
        BookRepositoryImpl(
            get<BookFetcher>(),
            get<BookLocalDataSource>(),
            get<DataStoreManager>()
        )
    }
    single<LibraryRepository> { LibraryRepositoryImpl(get<LibraryDao>()) }
}