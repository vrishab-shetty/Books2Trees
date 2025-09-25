package com.company.books2trees.di

import com.company.books2trees.data.local.database.BookDatabase
import com.company.books2trees.data.local.database.dao.LibraryBookDao
import com.company.books2trees.data.local.database.dao.RecentBookDao
import com.company.books2trees.data.local.datasource.api.LibraryLocalDataSource
import com.company.books2trees.data.local.datasource.api.PdfLocalDataSourceImpl
import com.company.books2trees.data.local.datasource.api.RecentBooksLocalDataSource
import com.company.books2trees.data.local.datasource.impl.LibraryLocalDataSourceImpl
import com.company.books2trees.data.local.datasource.impl.PdfLocalDataSource
import com.company.books2trees.data.local.datasource.impl.RecentBooksLocalDataSourceImpl
import com.company.books2trees.data.local.files.FileUtil
import com.company.books2trees.data.local.files.pdf.PdfPageProviderFactory
import com.company.books2trees.data.local.preferences.UserPreferences
import com.company.books2trees.data.local.preferences.UserPreferencesImpl
import com.company.books2trees.data.remote.datasource.api.BookRemoteDataSource
import com.company.books2trees.data.remote.datasource.impl.BookRemoteDataSourceImpl
import com.company.books2trees.data.repository.BookSearchRepositoryImpl
import com.company.books2trees.data.repository.LibraryRepositoryImpl
import com.company.books2trees.data.repository.PdfRepositoryImpl
import com.company.books2trees.data.repository.RecentBooksRepositoryImpl
import com.company.books2trees.data.repository.UserPreferencesRepositoryImpl
import com.company.books2trees.domain.repository.BookSearchRepository
import com.company.books2trees.domain.repository.LibraryRepository
import com.company.books2trees.domain.repository.PdfRepository
import com.company.books2trees.domain.repository.RecentBooksRepository
import com.company.books2trees.domain.repository.UserPreferencesRepository
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    // Low-Level Concerns (Database, DAOs, API services, /...)
    single { BookDatabase[androidContext()] }
    single { get<BookDatabase>().libraryBookDao() }
    single { get<BookDatabase>().recentBookDao() }

    single { FileUtil(context = androidContext()) }
    single { OkHttpClient() }
    single { Gson() }

    // High-Level Concerns (Repositories, Data Sources, /...)
    single<LibraryLocalDataSource> {
        LibraryLocalDataSourceImpl(get<LibraryBookDao>())
    }
    single<PdfLocalDataSource> { PdfLocalDataSourceImpl() }
    single<RecentBooksLocalDataSource> {
        RecentBooksLocalDataSourceImpl(get<RecentBookDao>())
    }
    single<BookRemoteDataSource> {
        BookRemoteDataSourceImpl(
            get<OkHttpClient>(),
            get<Gson>()
        )
    }
    single<UserPreferences> { UserPreferencesImpl(androidContext()) }
    single { PdfPageProviderFactory(androidContext()) }

    // Repositories
    single<BookSearchRepository> {
        BookSearchRepositoryImpl(
            get<BookRemoteDataSource>(),
        )
    }
    single<LibraryRepository> {
        LibraryRepositoryImpl(
            get<LibraryLocalDataSource>()
        )
    }
    single<PdfRepository> {
        PdfRepositoryImpl(
            get<FileUtil>(),
            get<PdfLocalDataSource>(),
            get<PdfPageProviderFactory>()
        )
    }
    single<RecentBooksRepository> {
        RecentBooksRepositoryImpl(
            get<RecentBooksLocalDataSource>()
        )
    }
    single<UserPreferencesRepository> {
        UserPreferencesRepositoryImpl(
            get<UserPreferences>()
        )
    }


}