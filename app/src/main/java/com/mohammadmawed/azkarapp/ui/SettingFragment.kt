package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first


@AndroidEntryPoint
class SettingFragment : Fragment() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var notificationSwitch: Switch
    private lateinit var settingUI: ConstraintLayout
    private lateinit var timePickerButton: RelativeLayout
    private lateinit var rateUsButton: RelativeLayout
    private lateinit var policyButton: RelativeLayout
    private lateinit var calendarSettingTextView: TextView
    private lateinit var notificationSetTextView: TextView

    private val viewModel: ZikrViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        notificationSwitch = view.findViewById(R.id.switch1)
        calendarSettingTextView = view.findViewById(R.id.calendarRecTextView)
        notificationSetTextView = view.findViewById(R.id.notificationSetTextView)
        settingUI = view.findViewById(R.id.settingUI)
        policyButton = view.findViewById(R.id.policyButton)
        timePickerButton = view.findViewById(R.id.timePickerButton)
        rateUsButton = view.findViewById(R.id.rateUsButton)


        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner) {
            calendarSettingTextView.text = it

        }

        var savedHour = 0
        var savedMinute = 0

        //Loading user's settings
        lifecycleScope.launchWhenStarted {

            savedHour = viewModel.notificationTimeHourFlow.first()
            savedMinute = viewModel.notificationTimeMinuteFlow.first()

            if (savedMinute == 9 || savedMinute < 9) {
                notificationSetTextView.text = "$savedHour:0$savedMinute"
            } else {
                notificationSetTextView.text = "$savedHour:$savedMinute"
            }

            viewModel.notificationRefFlow.collectLatest {
                notificationSwitch.isChecked = it
            }
        }

        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner) {
            calendarSettingTextView.text = it
        }

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.saveNotificationSettings(true, requireContext())
                lifecycleScope.launchWhenStarted {
                    viewModel.notificationsOnSharedFlow.collectLatest {
                        if (it) {
                            Snackbar.make(
                                settingUI,
                                R.string.notifications_are_now_ON,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            } else {
                // If the switch button is on
                viewModel.saveNotificationSettings(false, requireContext())
                Snackbar.make(
                    settingUI,
                    resources.getText(R.string.notifications_are_now_OFF),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        timePickerButton.setOnClickListener {

            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(savedHour)
                    .setMinute(savedMinute)
                    .setTitleText(R.string.select_time_to_get_notification_at)
                    .build()

            picker.show(childFragmentManager, Constants.TIME_PICKER_TAG)

            picker.addOnPositiveButtonClickListener {

                val hour = picker.hour
                val minute = picker.minute

                var mintOp = "$hour:$minute"

                if (minute == 9 || minute < 9) {
                    mintOp = "$hour:0$minute"
                }

                notificationSetTextView.text = mintOp

                viewModel.saveNotificationSettingsHour(hour, requireContext())
                viewModel.saveNotificationSettingsMinute(minute, requireContext())
                viewModel.saveNotificationSettings(true, requireContext())

                Snackbar.make(
                    settingUI,
                    R.string.you_will_receive_at,
                    Snackbar.LENGTH_LONG
                ).show()

            }
            picker.addOnNegativeButtonClickListener {
                // call back code
            }
            picker.addOnCancelListener {
                // call back code
            }
            picker.addOnDismissListener {
                // call back code
            }
        }

        policyButton.setOnClickListener {
            val uri: Uri = Uri.parse(Constants.PRIVACY_POLICY_URL)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        return view

    }

}