package com.company.books2trees.di

import android.app.Application
import com.google.android.gms.ads.MobileAds
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class KoinApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@KoinApp)
            modules(
                dataModule,
                adModule,
                authModule,
                domainModule,
                viewModelModule
            )
        }

        // This sets up the Google Mobile Ads SDK for the entire app lifecycle.
        MobileAds.initialize(this)
    }
}