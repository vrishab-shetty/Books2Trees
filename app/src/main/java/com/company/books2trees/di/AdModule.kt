package com.company.books2trees.di

import com.company.books2trees.data.sdk.AdManagerImpl
import com.company.books2trees.domain.ads.AdManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val adModule = module {
    single<AdManager> { AdManagerImpl(androidApplication()) }
}