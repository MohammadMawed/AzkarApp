package com.mohammadmawed.azkarapp.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.mohammadmawed.azkarapp.MainActivity
import com.mohammadmawed.azkarapp.R
import dagger.hilt.android.AndroidEntryPoint

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

        //Loading user's settings
        viewModel.notificationRefFlow.observe(viewLifecycleOwner, Observer {
            notificationSwitch.isChecked = it
        })

        viewModel.languagePrefFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == "ar") {
                radioButton.isChecked = true
            } else if (it == "en") {
                radioButton1.isChecked = true
            }
        })

        notificationSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                viewModel.saveNotificationSettings(true)
                Log.d("notificat sett checked", "true")
                //Snackbar.make(settingUI, "Notifications are now on!", Snackbar.LENGTH_LONG).show()
            } else {
                viewModel.saveNotificationSettings(false)
                Log.d("notificat sett uncheck", "false")
                //Snackbar.make(settingUI, "Notifications are now OFF!", Snackbar.LENGTH_LONG).show()
            }

        }

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

        radioButton.setOnClickListener {

            viewModel.saveLanguageSettings("ar")

            radioButton.isChecked = true

            viewModel.changeLanguage("ar", requireContext())

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            //Snackbar.make(settingUI, "Something went wrong!", Snackbar.LENGTH_LONG).show()

        }

        radioButton1.setOnClickListener {
            viewModel.saveLanguageSettings("en")

            viewModel.changeLanguage("en", requireContext())

            radioButton1.isChecked = true
            viewModel.changeLanguage("ar", requireContext())

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun isNightMode(context: Context): Boolean {
        val nightModeFlags =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

}