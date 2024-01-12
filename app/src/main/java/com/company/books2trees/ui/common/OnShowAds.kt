package com.company.books2trees.ui.common

interface OnShowAds {
    fun isInitialized(): Boolean

    fun loadAd()

    fun showAd(onRewardEarned:() -> Unit)
}