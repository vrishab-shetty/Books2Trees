package com.company.books2trees.ui.common

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object AppAdManager {

    private var TAG = "AppAdManager"
    private var rewardedAd: RewardedAd? = null
    private var isMobileAdsInitialized = false


    private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    fun initialize(context: Context) {
        if (isMobileAdsInitialized) return
        MobileAds.initialize(context) {
            isMobileAdsInitialized = true
            loadAd(context)
        }
    }

    fun isAdReady(): Boolean = rewardedAd != null

    fun loadAd(context: Context) {
        if (rewardedAd != null) return

        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            AD_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                }
            }
        )
    }

    fun showAd(activity: Activity, onRewardGranted: () -> Unit) {
        if (rewardedAd == null) {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
            loadAd(activity)
            return
        }

        var wasRewardGranted = false

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null

                if (wasRewardGranted) {
                    onRewardGranted()
                }

                loadAd(activity)
            }
        }

        rewardedAd?.show(activity) {
            wasRewardGranted = true
            Log.d(TAG, "user earned the reward.")
        }
    }

}