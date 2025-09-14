package com.company.books2trees

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.company.books2trees.data.repository.AuthRepository // CHANGED: Import the repository
import com.company.books2trees.databinding.ActivityMainBinding
import com.company.books2trees.presentation.common.OnViewProfile
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), OnViewProfile {

    private lateinit var binding: ActivityMainBinding
    private val authRepository: AuthRepository by inject()

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

    override fun getData() = authRepository.user.value

    override fun signOut(callback: () -> Unit) {
        lifecycleScope.launch {
            authRepository.signOut()
            callback.invoke()
        }
    }
}