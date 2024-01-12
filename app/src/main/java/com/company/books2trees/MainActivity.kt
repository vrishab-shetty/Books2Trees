package com.company.books2trees

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.company.books2trees.databinding.ActivityMainBinding
import com.company.books2trees.presentation.sign_in.GoogleAuthUiClient
import com.company.books2trees.ui.common.OnShowAds
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnViewProfile, OnShowAds {

    private var TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private var isMobileAdsInitialized = false
    private var rewardedAd: RewardedAd? = null


    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {
            isMobileAdsInitialized = true
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavbar.setupWithNavController(navController)
        binding.navRailView.setupWithNavController(navController)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.navRailView.visibility = View.GONE
        } else {
            binding.bottomNavbar.visibility = View.GONE
        }

    }

    override fun getData() = googleAuthUiClient.getSignedInUser()
    override fun signOut(callback: () -> Unit) {
        lifecycleScope.launch {
            googleAuthUiClient.signOut()
            callback.invoke()
        }
    }

    override fun isInitialized(): Boolean = isMobileAdsInitialized
    override fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            this,
            "ca-app-pub-3940256099942544/5224354917",
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
            })
    }

    override fun showAd(onRewardEarned: () -> Unit) {


        rewardedAd?.let { ad ->

            ad.fullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    loadAd()
                }
            }
            ad.show(this) { rewardItem ->
                // Handle the reward.
//                val rewardAmount = rewardItem.amount
//                val rewardType = rewardItem.type

                onRewardEarned()
                Log.d(TAG, "User earned the reward.")

            }
        } ?: run {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    }


}