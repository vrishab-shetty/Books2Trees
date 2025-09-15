package com.company.books2trees

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.company.books2trees.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
}