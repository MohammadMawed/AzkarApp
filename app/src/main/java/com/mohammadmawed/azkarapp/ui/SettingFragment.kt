package com.mohammadmawed.azkarapp.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.mohammadmawed.azkarapp.R

class SettingFragment : Fragment() {

    private lateinit var notificationSwitch: Switch
    private lateinit var darkModeSwitch: Switch
    private lateinit var settingUI: ConstraintLayout
    private lateinit var calendarSettingTextView: TextView
    private lateinit var viewModel: ZikrViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        notificationSwitch = view.findViewById(R.id.switch1)
        darkModeSwitch = view.findViewById(R.id.switch2)
        calendarSettingTextView = view.findViewById(R.id.calendarSettingTextView)
        settingUI = view.findViewById(R.id.settingUI)


        viewModel = ViewModelProvider(requireActivity())[ZikrViewModel::class.java]

        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner, Observer {
            calendarSettingTextView.text = it
        })

        darkModeSwitch.isChecked = isNightMode(requireContext())

        darkModeSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        notificationSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                //viewModel.reminderNotification()
                Snackbar.make(settingUI, "Notifications are now on!", Snackbar.LENGTH_LONG).show()

            }
        }

        fun onRadioButtonClicked(view: View) {
            if (view is RadioButton) {
                // Is the button now checked?
                val checked = view.isChecked

                // Check which radio button was clicked
                when (view.getId()) {
                    R.id.radio_arabic ->
                        if (checked) {
                            // Pirates are the best

                        }
                    R.id.radio_english ->
                        if (checked) {
                            // Ninjas rule

                        }
                }
            }
        }
        return view
    }
    fun isNightMode(context: Context): Boolean {
        val nightModeFlags =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

}