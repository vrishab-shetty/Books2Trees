package com.company.books2trees.di

import com.company.books2trees.domain.repository.AuthRepository
import com.company.books2trees.domain.repository.BookRepository
import com.company.books2trees.domain.repository.LibraryRepository
import com.company.books2trees.domain.use_case.AddRecentBookUseCase
import com.company.books2trees.domain.use_case.DeleteLibraryBookUseCase
import com.company.books2trees.domain.use_case.GetGenresUseCase
import com.company.books2trees.domain.use_case.GetHomePageBooksUseCase
import com.company.books2trees.domain.use_case.GetProfileContentUseCase
import com.company.books2trees.domain.use_case.GetRecentBooksUseCase
import com.company.books2trees.domain.use_case.GetSearchFilterUseCase
import com.company.books2trees.domain.use_case.GetSignedInUserUseCase
import com.company.books2trees.domain.use_case.InsertBookToLibraryUseCase
import com.company.books2trees.domain.use_case.RemoveRecentBookUseCase
import com.company.books2trees.domain.use_case.SearchBooksUseCase
import com.company.books2trees.domain.use_case.SetSearchFilterUseCase
import com.company.books2trees.domain.use_case.SignInWithGoogleTokenUseCase
import com.company.books2trees.domain.use_case.SignOutUseCase
import com.company.books2trees.domain.use_case.UpdateLibraryItemUseCase
import org.koin.dsl.module

val domainModule = module {
    // Use `factory` for stateless Use Cases
    factory { AddRecentBookUseCase(get<BookRepository>()) }
    factory { DeleteLibraryBookUseCase(get<LibraryRepository>()) }
    factory { GetGenresUseCase(get<BookRepository>()) }
    factory { GetHomePageBooksUseCase(get<BookRepository>()) }
    factory { GetProfileContentUseCase(get<LibraryRepository>()) }
    factory { GetRecentBooksUseCase(get<BookRepository>()) }
    factory { GetSearchFilterUseCase(get<BookRepository>()) }
    factory { InsertBookToLibraryUseCase(get<LibraryRepository>()) }
    factory { RemoveRecentBookUseCase(get<BookRepository>()) }
    factory { SearchBooksUseCase(get<BookRepository>()) }
    factory { SetSearchFilterUseCase(get<BookRepository>()) }
    factory { UpdateLibraryItemUseCase(get<LibraryRepository>()) }
    factory { GetSignedInUserUseCase(get<AuthRepository>()) }
    factory { SignInWithGoogleTokenUseCase(get<AuthRepository>()) }
    factory { SignOutUseCase(get<AuthRepository>()) }

}