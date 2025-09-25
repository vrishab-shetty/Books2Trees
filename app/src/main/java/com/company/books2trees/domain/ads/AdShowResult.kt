package com.company.books2trees.domain.ads

sealed class AdShowResult {
    data class Success(val rewardGranted: Boolean) : AdShowResult()
    object AdNotReady : AdShowResult()
}