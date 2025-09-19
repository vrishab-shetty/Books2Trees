package com.company.books2trees.di

import com.company.books2trees.data.local.BookDatabase
import com.company.books2trees.data.local.BookLocalDataSource
import com.company.books2trees.data.local.DataStoreManager
import com.company.books2trees.data.local.LibraryDao
import com.company.books2trees.data.local.PdfLocalDataSource
import com.company.books2trees.data.local.PdfPageProviderFactory
import com.company.books2trees.data.local.RecentBookDao
import com.company.books2trees.data.remote.BookFetcher
import com.company.books2trees.data.repository.BookRepositoryImpl
import com.company.books2trees.data.repository.LibraryRepositoryImpl
import com.company.books2trees.data.repository.PdfRepositoryImpl
import com.company.books2trees.data.utils.FileUtil
import com.company.books2trees.domain.repository.BookRepository
import com.company.books2trees.domain.repository.LibraryRepository
import com.company.books2trees.domain.repository.PdfRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    // Low-Level Concerns (Database, DAOs, API services, /...)
    single { BookDatabase[androidContext()] }
    single { get<BookDatabase>().libraryDao() }
    single { get<BookDatabase>().recentDao() }
    single { FileUtil(context = androidContext()) }
    single { BookFetcher }
    single { DataStoreManager(androidContext()) }
    single { BookLocalDataSource(get<RecentBookDao>()) }
    single { PdfLocalDataSource() }
    single { PdfPageProviderFactory(androidContext()) }

    // Repositories
    single<BookRepository> {
        BookRepositoryImpl(
            get<BookFetcher>(),
            get<BookLocalDataSource>(),
            get<DataStoreManager>()
        )
    }
    single<LibraryRepository> { LibraryRepositoryImpl(get<LibraryDao>()) }
    single<PdfRepository> {
        PdfRepositoryImpl(
            get<FileUtil>(),
            get<PdfLocalDataSource>(),
            get<PdfPageProviderFactory>()
        )
    }
}