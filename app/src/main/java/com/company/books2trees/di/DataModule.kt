package com.company.books2trees.di

import com.company.books2trees.data.local.database.BookDatabase
import com.company.books2trees.data.local.database.dao.LibraryBookDao
import com.company.books2trees.data.local.database.dao.RecentBookDao
import com.company.books2trees.data.local.datasource.api.BookLocalDataSource
import com.company.books2trees.data.local.datasource.api.PdfLocalDataSource
import com.company.books2trees.data.local.datasource.impl.BookLocalDataSourceImpl
import com.company.books2trees.data.local.files.FileUtil
import com.company.books2trees.data.local.files.pdf.PdfPageProviderFactory
import com.company.books2trees.data.local.preferences.UserPreferences
import com.company.books2trees.data.local.preferences.UserPreferencesImpl
import com.company.books2trees.data.remote.datasource.api.BookRemoteDataSource
import com.company.books2trees.data.remote.datasource.impl.BookRemoteDataSourceImpl
import com.company.books2trees.data.repository.BookRepositoryImpl
import com.company.books2trees.data.repository.PdfRepositoryImpl
import com.company.books2trees.domain.repository.BookRepository
import com.company.books2trees.domain.repository.PdfRepository
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    // Low-Level Concerns (Database, DAOs, API services, /...)
    single { BookDatabase[androidContext()] }
    single { get<BookDatabase>().libraryDao() }
    single { get<BookDatabase>().recentDao() }
    single { FileUtil(context = androidContext()) }
    single { OkHttpClient() }
    single { Gson() }
    single<BookRemoteDataSource> {
        BookRemoteDataSourceImpl(
            get<OkHttpClient>(),
            get<Gson>()
        )
    }
    single<UserPreferences> { UserPreferencesImpl(androidContext()) }
    single<BookLocalDataSource> {
        BookLocalDataSourceImpl(
            get<RecentBookDao>(),
            get<LibraryBookDao>()
        )
    }
    single { PdfLocalDataSource() }
    single { PdfPageProviderFactory(androidContext()) }

    // Repositories
    single<BookRepository> {
        BookRepositoryImpl(
            get<BookRemoteDataSource>(),
            get<BookLocalDataSource>(),
            get<UserPreferences>()
        )
    }
    single<PdfRepository> {
        PdfRepositoryImpl(
            get<FileUtil>(),
            get<PdfLocalDataSource>(),
            get<PdfPageProviderFactory>()
        )
    }
}