package com.company.books2trees.ui.common

interface AdHandler {
    fun isInitialized(): Boolean

    fun loadAd()

    fun showAd(onRewardEarned:() -> Unit)
}