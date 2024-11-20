package com.mvvmproject.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mvvmproject.R
import com.mvvmproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val topLevelDestinationIds: Set<Int> = setOf(
            R.id.homeFragment,
            R.id.programsFragment,
            R.id.savedFragment
        )

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.bottomNav.isVisible = topLevelDestinationIds.contains(destination.id)
        }
    }
}