package com.company.books2trees.di

import androidx.credentials.CredentialManager
import com.company.books2trees.data.auth.GoogleAuthUiClient
import com.company.books2trees.data.repository.AuthRepository
import com.company.books2trees.presentation.signin.SignInViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val authModule = module {
    single { CredentialManager.create(androidContext()) }
    single { GoogleAuthUiClient(androidContext(), get<CredentialManager>()) }

    single { AuthRepository(get<GoogleAuthUiClient>()) }
    viewModel { SignInViewModel(get<AuthRepository>()) }
}