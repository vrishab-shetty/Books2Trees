package com.company.books2trees.data.sdk

import android.app.Activity
import android.content.Context
import com.company.books2trees.BuildConfig
import com.company.books2trees.domain.model.AdResult
import com.company.books2trees.domain.repository.AdRepository
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AdManagerImpl(private val context: Context) : AdRepository {

    val adUnitId = BuildConfig.REWARDED_AD_UNIT_ID

    override suspend fun showRewardedAd(activity: Activity): AdResult {
        return suspendCancellableCoroutine { continuation ->
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                context,
                adUnitId,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {

                        var wasRewardEarned = false

                        ad.fullScreenContentCallback = object : FullScreenContentCallback() {

                            override fun onAdDismissedFullScreenContent() {
                                if (continuation.isActive) {
                                    if (wasRewardEarned) {
                                        continuation.resume(AdResult.Success)
                                    } else {
                                        continuation.resume(AdResult.Failed)
                                    }
                                }
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                if (continuation.isActive) {
                                    continuation.resume(AdResult.Failed)

                                }
                            }
                        }

                        val rewardListener = OnUserEarnedRewardListener {
                            wasRewardEarned = true
                        }

                        ad.show(activity, rewardListener)
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        if (continuation.isActive) {
                            continuation.resume(AdResult.Failed)
                        }
                    }
                }
            )
        }
    }


}