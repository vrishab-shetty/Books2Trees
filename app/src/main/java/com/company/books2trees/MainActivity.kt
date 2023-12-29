package com.company.books2trees

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.company.books2trees.databinding.ActivityMainBinding
import com.company.books2trees.presentation.sign_in.GoogleAuthUiClient
import com.company.books2trees.presentation.sign_in.UserData
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnViewProfile {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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