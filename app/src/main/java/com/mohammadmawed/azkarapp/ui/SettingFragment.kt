package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Switch
import androidx.lifecycle.ViewModelProvider
import com.mohammadmawed.azkarapp.R

class SettingFragment : Fragment() {

    private lateinit var notificationSwitch: Switch
    private lateinit var viewModel: ZikrViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        notificationSwitch = view.findViewById(R.id.switch1)


        viewModel = ViewModelProvider(requireActivity())[ZikrViewModel::class.java]



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

}