package com.mohammadmawed.azkarapp.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.mohammadmawed.azkarapp.MainActivity
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var notificationSwitch: Switch
    private lateinit var darkModeSwitch: Switch
    private lateinit var radioButton: RadioButton
    private lateinit var radioButton1: RadioButton
    private lateinit var radioButtonG: RadioGroup
    private lateinit var settingUI: ConstraintLayout
    private lateinit var calendarSettingTextView: TextView

    private val viewModel: ZikrViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        notificationSwitch = view.findViewById(R.id.switch1)
        darkModeSwitch = view.findViewById(R.id.switch2)
        radioButton = view.findViewById(R.id.radio_arabic)
        radioButton1 = view.findViewById(R.id.radio_english)
        radioButtonG = view.findViewById(R.id.grr)
        calendarSettingTextView = view.findViewById(R.id.calendarSettingTextView)
        settingUI = view.findViewById(R.id.settingUI)


        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner) {
            calendarSettingTextView.text = it
        }

        darkModeSwitch.isChecked = isNightMode(requireContext())

        darkModeSwitch.setOnCheckedChangeListener { _, b ->
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        notificationSwitch.setOnCheckedChangeListener { _, b ->
            if (b) {
                Snackbar.make(settingUI, "Notifications are now on!", Snackbar.LENGTH_LONG).show()
            }
        }

        radioButton.setOnClickListener {

            val locale = Locale("ar")
            Locale.setDefault(locale)

            val resources: Resources = requireContext().resources

            val configuration: Configuration = resources.configuration
            configuration.locale = locale
            configuration.setLayoutDirection(locale)

            resources.updateConfiguration(configuration, resources.displayMetrics)
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)

            radioButton.isChecked = true
        }


        radioButton1.setOnClickListener {

            val locale = Locale("en")
            Locale.setDefault(locale)

            val resources: Resources = requireContext().resources

            val configuration: Configuration = resources.configuration
            configuration.locale = locale
            configuration.setLayoutDirection(locale)

            resources.updateConfiguration(configuration, resources.displayMetrics)
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            radioButton1.isChecked = true
        }

        return view
    }

    fun isNightMode(context: Context): Boolean {
        val nightModeFlags =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

}