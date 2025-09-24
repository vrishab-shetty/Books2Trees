package com.company.books2trees.domain.ads

import android.app.Activity

interface AdManager {
    fun isAdReady(): Boolean
    fun showAd(activity: Activity, onAdFlowFinished: (wasRewardGranted: Boolean) -> Unit)
}