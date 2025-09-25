package com.company.books2trees.data.sdk

import android.app.Activity
import android.app.Application
import com.company.books2trees.BuildConfig
import com.company.books2trees.domain.ads.AdManager
import com.company.books2trees.domain.ads.AdShowResult
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AdManagerImpl(private val application: Application) : AdManager {

    private var rewardedAd: RewardedAd? = null
    private val adUnitId = BuildConfig.REWARDED_AD_UNIT_ID

    init {
        loadAd()
    }

    private fun loadAd() {
        if (rewardedAd != null) return

        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            application,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun showAd(activity: Activity): AdShowResult {
        return suspendCancellableCoroutine { continuation ->
            if (rewardedAd == null) {
                loadAd() // Try to load the next one
                continuation.resume(AdShowResult.AdNotReady)
                return@suspendCancellableCoroutine
            }

            var wasRewardGranted = false
            rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // When the ad is dismissed, the flow is over. Resume the coroutine.
                    if (continuation.isActive) {
                        continuation.resume(AdShowResult.Success(wasRewardGranted))
                    }
                    rewardedAd = null
                    loadAd() // Pre-load the next ad
                }
            }

            rewardedAd?.show(activity) {
                // User earned the reward
                wasRewardGranted = true
            }
        }
    }

}