package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
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

    private lateinit var notificationSecSwitch: Switch
    private lateinit var settingUI: ConstraintLayout
    private lateinit var settingUI1: ConstraintLayout

    private lateinit var timePickerButton: RelativeLayout
    private lateinit var timePickerButton1: RelativeLayout

    private lateinit var rateUsButton: RelativeLayout
    private lateinit var policyButton: RelativeLayout

    private lateinit var calendarSettingTextView: TextView
    private lateinit var notificationSetTextView: TextView
    private lateinit var notificationSecSetTextView: TextView

    private lateinit var versionTextView: TextView

    private val viewModel: ZikrViewModel by viewModels()

    @SuppressLint("SetTextI18n", "StringFormatInvalid")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        notificationSwitch = view.findViewById(R.id.switch1)
        notificationSecSwitch = view.findViewById(R.id.switch2)
        calendarSettingTextView = view.findViewById(R.id.calendarRecTextView)
        notificationSetTextView = view.findViewById(R.id.notificationSetTextView)
        notificationSecSetTextView = view.findViewById(R.id.notificationSecSetTextView)
        settingUI = view.findViewById(R.id.settingUI)
        policyButton = view.findViewById(R.id.policyButton)
        timePickerButton = view.findViewById(R.id.timePickerButton)
        timePickerButton1 = view.findViewById(R.id.timePickerSecButton)
        rateUsButton = view.findViewById(R.id.rateUsButton)
        versionTextView = view.findViewById(R.id.versionTextView)


        versionTextView.text = Constants.APP_VERSION

        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner) {
            calendarSettingTextView.text = it

        }

        var savedHour = 0
        var savedMinute = 0

        var savedHourSec = 0
        var savedMinuteSec = 0

        var switchSate1 = false
        var switchSate2 = false



        lifecycleScope.launchWhenStarted {
            // Initialize variables for saved settings
            savedHour = viewModel.notificationTimeHourFlow.first()
            savedMinute = viewModel.notificationTimeMinuteFlow.first()
            savedHourSec = viewModel.notificationSecTimeHourFlow.first()
            savedMinuteSec = viewModel.notificationSecTimeMinuteFlow.first()

            // Update UI based on saved settings
            updateNotificationTextView(savedHour, savedMinute, notificationSetTextView)
            updateNotificationTextView(savedHourSec, savedMinuteSec, notificationSecSetTextView)

            Log.d("Checking", "Loading the switch1 state")
            switchSate1 = viewModel.notificationRefFlow.first()
            notificationSwitch.isChecked = switchSate1

            Log.d("Checking", "Loading the switch2 state")
            switchSate2 = viewModel.notificationSecRefFlow.first()
            notificationSecSwitch.isChecked = switchSate2

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

        notificationSecSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.saveNotificationSecSettings(true, requireContext())
                Log.d("TAG", "Debug log message");
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
                viewModel.saveNotificationSecSettings(false, requireContext())
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

                val name = getString(R.string.you_will_receive_at)

                Snackbar.make(settingUI, "$name $mintOp", Snackbar.LENGTH_LONG).show()

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
        // Inside timePickerButton1's setOnClickListener
        timePickerButton1.setOnClickListener {
            val pickerSec = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(savedHourSec)
                .setMinute(savedMinuteSec)
                .setTitleText(R.string.select_time_to_get_notification_at)
                .build()

            pickerSec.show(childFragmentManager, Constants.TIME_PICKER_SEC_TAG) // Use a different tag to differentiate from the first picker

            pickerSec.addOnPositiveButtonClickListener {
                val hourSec = pickerSec.hour
                val minuteSec = pickerSec.minute

                var mintOpSec = if (minuteSec <= 9) {
                    "$hourSec:0$minuteSec"
                } else {
                    "$hourSec:$minuteSec"
                }

                notificationSecSetTextView.text = mintOpSec

                // Saving the second notification settings correctly
                viewModel.saveNotificationSecSettingsHour(hourSec, requireContext())
                viewModel.saveNotificationSecSettingsMinute(minuteSec, requireContext())
                viewModel.saveNotificationSecSettings(true, requireContext())

                val name = getString(R.string.you_will_receive_at)


                Snackbar.make(settingUI, "$name $mintOpSec", Snackbar.LENGTH_LONG).show()
            }
            // Add negative, cancel, and dismiss listeners as needed
            pickerSec.addOnNegativeButtonClickListener {
                // call back code
            }
            pickerSec.addOnCancelListener {
                // call back code
            }
            pickerSec.addOnDismissListener {
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


    // Helper function to update notification text views
    private fun updateNotificationTextView(hour: Int, minute: Int, textView: TextView) {
        val formattedTime = if (minute <= 9) "$hour:0$minute" else "$hour:$minute"
        textView.text = formattedTime
    }

}