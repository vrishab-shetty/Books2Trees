package com.company.books2trees.domain.ads

import android.app.Activity

interface AdManager {
    fun isAdReady(): Boolean
    suspend fun showAd(activity: Activity): AdShowResult
}