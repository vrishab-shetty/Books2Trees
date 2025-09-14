package com.company.books2trees.di

import com.company.books2trees.domain.ads.AdManager
import com.company.books2trees.domain.use_case.AddRecentBookUseCase
import com.company.books2trees.domain.use_case.DeleteLibraryBookUseCase
import com.company.books2trees.domain.use_case.GetGenresUseCase
import com.company.books2trees.domain.use_case.GetHomePageBooksUseCase
import com.company.books2trees.domain.use_case.GetProfileContentUseCase
import com.company.books2trees.domain.use_case.GetRecentBooksUseCase
import com.company.books2trees.domain.use_case.GetSearchFilterUseCase
import com.company.books2trees.domain.use_case.InsertBookToLibraryUseCase
import com.company.books2trees.domain.use_case.RemoveRecentBookUseCase
import com.company.books2trees.domain.use_case.SearchBooksUseCase
import com.company.books2trees.domain.use_case.SetSearchFilterUseCase
import com.company.books2trees.domain.use_case.UpdateLibraryItemUseCase
import com.company.books2trees.presentation.home.HomeViewModel
import com.company.books2trees.presentation.profile.ProfileViewModel
import com.company.books2trees.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // HomeViewModel and its specific Use Case dependencies
    viewModel {
        HomeViewModel(
            getHomePageBooks = get<GetHomePageBooksUseCase>(),
            getRecentBooks = get<GetRecentBooksUseCase>(),
            addRecentBook = get<AddRecentBookUseCase>(),
            removeRecentBook = get<RemoveRecentBookUseCase>(),
            insertBookToLibrary = get<InsertBookToLibraryUseCase>(),
            adManager = get<AdManager>()
        )
    }

    // ProfileViewModel and its dependencies
    viewModel {
        ProfileViewModel(
            getProfileContent = get<GetProfileContentUseCase>(),
            addRecentBook = get<AddRecentBookUseCase>(),
            deleteLibraryBook = get<DeleteLibraryBookUseCase>(),
            updateLibraryItem = get<UpdateLibraryItemUseCase>()
        )
    }

    // SearchViewModel and its dependencies
    viewModel {
        SearchViewModel(
            searchBooks = get<SearchBooksUseCase>(),
            addRecentBook = get<AddRecentBookUseCase>(),
            getGenres = get<GetGenresUseCase>(),
            getSearchFilter = get<GetSearchFilterUseCase>(),
            setSearchFilter = get<SetSearchFilterUseCase>()
        )
    }
}