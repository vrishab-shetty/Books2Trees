package com.company.books2trees.di

import com.company.books2trees.data.ads.AppAdManagerImpl
import com.company.books2trees.domain.ads.AdManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val adModule = module {
    single<AdManager> { AppAdManagerImpl(androidApplication()) }
}