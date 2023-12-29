package com.company.books2trees

import android.app.Application
import com.company.books2trees.repos.BookRepository
import com.company.books2trees.ui.profile.ProfileViewModel
import com.company.books2trees.repos.LibraryRepository
import com.company.books2trees.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val MODULE = module {
    single { LibraryRepository(androidContext().applicationContext) }
    single { BookRepository(androidContext().applicationContext) }
    viewModel {
        ProfileViewModel(get<LibraryRepository>(), get<BookRepository>())
    }
    viewModel {
        HomeViewModel(get<LibraryRepository>(), get<BookRepository>())
    }

}

class KoinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@KoinApp)
            modules(MODULE)
        }
    }
}