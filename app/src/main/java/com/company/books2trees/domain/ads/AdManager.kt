package com.company.books2trees.domain.ads

import android.app.Activity

interface AdManager {
    suspend fun showAd(activity: Activity): AdShowResult
}