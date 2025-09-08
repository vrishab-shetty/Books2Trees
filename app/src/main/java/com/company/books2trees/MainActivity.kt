package com.company.books2trees

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.company.books2trees.databinding.ActivityMainBinding
import com.company.books2trees.presentation.sign_in.GoogleAuthUiClient
import com.company.books2trees.ui.common.AppAdManager
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnViewProfile {


    private lateinit var binding: ActivityMainBinding


    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppAdManager.initialize(this)

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

}