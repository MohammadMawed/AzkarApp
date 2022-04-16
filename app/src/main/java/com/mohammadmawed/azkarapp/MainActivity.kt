package com.mohammadmawed.azkarapp

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mohammadmawed.azkarapp.data.PreferencesManager
import com.mohammadmawed.azkarapp.ui.MainUIFragment
import com.mohammadmawed.azkarapp.ui.SettingFragment
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var prefM: PreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefM = PreferencesManager(applicationContext)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_menu)

        setupWithNavController(bottomNavigationView, navController)

        lifecycleScope.launch {
            val vale = prefM.read("btn")
            if (vale == "true"){
                val locale = Locale("ar")
                Locale.setDefault(locale)

                val resources: Resources = applicationContext.resources

                val configuration: Configuration = resources.configuration
                configuration.locale = locale
                configuration.setLayoutDirection(locale)
            }
        }

    }
}