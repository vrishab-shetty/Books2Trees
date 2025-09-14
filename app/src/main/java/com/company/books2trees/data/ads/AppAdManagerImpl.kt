package com.company.books2trees.data.ads

import android.app.Activity
import android.app.Application
import com.company.books2trees.domain.ads.AdManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AppAdManagerImpl(private val application: Application) : AdManager {

    private var rewardedAd: RewardedAd? = null
    private val AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    init {
        loadAd()
    }

    override fun isAdReady(): Boolean = rewardedAd != null

    private fun loadAd() {
        if (rewardedAd != null) return

        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            application,
            AD_UNIT_ID,
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

    override fun showAd(activity: Activity, onAdFlowFinished: (wasRewarded: Boolean) -> Unit) {
        if (rewardedAd == null) {
            onAdFlowFinished(false)
            loadAd()
            return
        }

        var wasRewardGranted = false
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onAdFlowFinished(wasRewardGranted)
                rewardedAd = null
                loadAd()
            }
        }

        rewardedAd?.show(activity) { rewardItem ->
            wasRewardGranted = true
        } ?: onAdFlowFinished(false)
    }

}