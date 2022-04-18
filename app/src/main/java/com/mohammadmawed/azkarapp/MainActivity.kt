package com.mohammadmawed.azkarapp

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mohammadmawed.azkarapp.ui.ZikrViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val viewModel: ZikrViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_menu)

        setupWithNavController(bottomNavigationView, navController)

        viewModel.languagePrefFlow.observe(this, androidx.lifecycle.Observer {
            if (it == "ar") {
                viewModel.changeLanguage("ar", application)

                val locale = Locale("ar")
                Locale.setDefault(locale)
                val resources: Resources? = application.resources
                val configuration: Configuration? = resources?.configuration
                configuration?.locale = locale
                configuration?.setLayoutDirection(locale)
                resources?.updateConfiguration(configuration, resources.displayMetrics)

            }else if (it == "en"){
                viewModel.changeLanguage("en", application)

                val locale = Locale("en")
                Locale.setDefault(locale)
                val resources: Resources? = application.resources
                val configuration: Configuration? = resources?.configuration
                configuration?.locale = locale
                configuration?.setLayoutDirection(locale)
                resources?.updateConfiguration(configuration, resources.displayMetrics)
            }
        })

    }
}