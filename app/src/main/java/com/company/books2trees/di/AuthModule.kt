package com.company.books2trees.di

import com.company.books2trees.data.auth.impl.AuthRepositoryImpl
import com.company.books2trees.domain.repository.AuthRepository
import org.koin.dsl.module


val authModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl()
    }
}