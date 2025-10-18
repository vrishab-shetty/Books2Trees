package com.company.books2trees.domain.repository

import android.app.Activity
import com.company.books2trees.domain.model.AdResult

interface AdRepository {
    suspend fun showRewardedAd(activity: Activity): AdResult
}