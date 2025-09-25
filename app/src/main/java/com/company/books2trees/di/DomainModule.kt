package com.company.books2trees.di

import com.company.books2trees.data.local.files.FileUtil
import com.company.books2trees.data.local.files.pdf.PdfPageProviderFactory
import com.company.books2trees.domain.repository.AuthRepository
import com.company.books2trees.domain.repository.BookSearchRepository
import com.company.books2trees.domain.repository.LibraryRepository
import com.company.books2trees.domain.repository.PdfRepository
import com.company.books2trees.domain.repository.RecentBooksRepository
import com.company.books2trees.domain.repository.UserPreferencesRepository
import com.company.books2trees.domain.use_case.AddBookToLibraryUseCase
import com.company.books2trees.domain.use_case.AddPdfUseCase
import com.company.books2trees.domain.use_case.AddRecentBookUseCase
import com.company.books2trees.domain.use_case.ClosePdfUseCase
import com.company.books2trees.domain.use_case.GetGenresUseCase
import com.company.books2trees.domain.use_case.GetHomePageBooksUseCase
import com.company.books2trees.domain.use_case.GetPdfPageBitmapUseCase
import com.company.books2trees.domain.use_case.GetPdfsUseCase
import com.company.books2trees.domain.use_case.GetProfileContentUseCase
import com.company.books2trees.domain.use_case.GetRecentBooksUseCase
import com.company.books2trees.domain.use_case.GetSearchFilterUseCase
import com.company.books2trees.domain.use_case.GetSignedInUserUseCase
import com.company.books2trees.domain.use_case.OpenPdfUseCase
import com.company.books2trees.domain.use_case.RemoveLibraryBookUseCase
import com.company.books2trees.domain.use_case.RemovePdfUseCase
import com.company.books2trees.domain.use_case.RemoveRecentBookUseCase
import com.company.books2trees.domain.use_case.SearchBooksUseCase
import com.company.books2trees.domain.use_case.SetSearchFilterUseCase
import com.company.books2trees.domain.use_case.SignInWithGoogleTokenUseCase
import com.company.books2trees.domain.use_case.SignOutUseCase
import com.company.books2trees.domain.use_case.UpdateLibraryItemUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {
    // Use `factory` for stateless Use Cases
    factory { AddRecentBookUseCase(get<RecentBooksRepository>()) }
    factory { RemoveLibraryBookUseCase(get<LibraryRepository>()) }
    factory { GetGenresUseCase(get<UserPreferencesRepository>()) }
    factory { GetHomePageBooksUseCase(get<BookSearchRepository>()) }
    factory { GetProfileContentUseCase(get<LibraryRepository>()) }
    factory { GetRecentBooksUseCase(get<RecentBooksRepository>()) }
    factory { GetSearchFilterUseCase(get<UserPreferencesRepository>()) }
    factory { AddBookToLibraryUseCase(get<LibraryRepository>()) }
    factory { RemoveRecentBookUseCase(get<RecentBooksRepository>()) }
    factory { SearchBooksUseCase(get<BookSearchRepository>()) }
    factory { SetSearchFilterUseCase(get<UserPreferencesRepository>()) }
    factory { UpdateLibraryItemUseCase(get<LibraryRepository>()) }
    factory { GetSignedInUserUseCase(get<AuthRepository>()) }
    factory { SignInWithGoogleTokenUseCase(get<AuthRepository>()) }
    factory { SignOutUseCase(get<AuthRepository>()) }
    factory { ClosePdfUseCase() }
    factory { GetPdfPageBitmapUseCase() }
    factory { OpenPdfUseCase(androidContext()) }
    factory { GetPdfsUseCase(get<PdfRepository>()) }
    factory { AddPdfUseCase(get<PdfRepository>(), get<PdfPageProviderFactory>(), get<FileUtil>()) }
    factory { RemovePdfUseCase(get<PdfRepository>(), get<FileUtil>()) }
}