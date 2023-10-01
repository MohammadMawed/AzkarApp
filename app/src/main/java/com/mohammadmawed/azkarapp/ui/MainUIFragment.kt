package com.mohammadmawed.azkarapp.ui

import android.Manifest
import android.annotation.SuppressLint

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.Zikr
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainUIFragment : Fragment() {

    private lateinit var zikrTextView: TextView
    private lateinit var hintTextView: TextView
    private lateinit var indexTextView: TextView
    private lateinit var repeatTimeTextView: TextView
    private lateinit var navigationTextView: TextView
    private lateinit var calendarTextView: TextView
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var alsabahChip: Chip
    private lateinit var almasahChip: Chip
    private lateinit var shareFloatingButton: FloatingActionButton
    private lateinit var zikrContainer: RelativeLayout

    //private lateinit var nav_menu: BottomNavigationView

    private val viewModel: ZikrViewModel by viewModels()
    lateinit var zikr: Zikr


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_u_i, container, false)


        zikrTextView = view.findViewById(R.id.zikrTextView)
        hintTextView = view.findViewById(R.id.hintTextView)
        indexTextView = view.findViewById(R.id.indexTextView)
        repeatTimeTextView = view.findViewById(R.id.repeatTimeTextView)
        navigationTextView = view.findViewById(R.id.navigationTextView)
        calendarTextView = view.findViewById(R.id.calendarTextView)
        nextButton = view.findViewById(R.id.nextButton)
        previousButton = view.findViewById(R.id.previousButton)
        almasahChip = view.findViewById(R.id.almasahChip)
        alsabahChip = view.findViewById(R.id.alsabahChip)
        shareFloatingButton = view.findViewById(R.id.shareFloatingButton)
        zikrContainer = view.findViewById(R.id.zikrContainer)

        val dialog = viewModel.visiblePermissionDialog

        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner) {
            calendarTextView.text = it
        }

        var idd = 1

        fun displayItem(idd: Int) {
            viewModel.itemById(idd, false).asLiveData().observe(viewLifecycleOwner) { zikrs ->
                val zikr = zikrs.firstOrNull() ?: return@observe
                zikrTextView.text = zikr.text
                hintTextView.text = zikr.hint
                repeatTimeTextView.text = "${zikr.repeat}X"
                indexTextView.text = "$idd/30"
            }
        }
        /*val selectedItemId: Int = nav_menu.selectedItemId
        val badge = nav_menu.getOrCreateBadge(selectedItemId)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number is set*/

        viewModel.itemById(idd, false).asLiveData().observe(viewLifecycleOwner) { list ->
            for (zikr in list) {
                zikrTextView.text = zikr.text
                hintTextView.text = zikr.hint
                repeatTimeTextView.text = zikr.repeat.toString() + "X"
                indexTextView.text = "$idd/30"
            }

        }

        nextButton.setOnClickListener {
            idd = (idd % 30) + 1
            displayItem(idd)
        }

        previousButton.setOnClickListener {
            idd = if (idd > 1) idd - 1 else 30
            displayItem(idd)
        }


        fun setListenersForChip(
            checkedChip: Chip,
            uncheckedChip: Chip,
            idRange: IntRange,
            displayText: String
        ) {
            checkedChip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    uncheckedChip.isChecked = false
                    navigationTextView.text = displayText

                    displayItem(idRange.first)

                    nextButton.setOnClickListener {
                        idd = (idd % idRange.last) + 1
                        displayItem(idd)
                    }

                    var iddd = 31
                    previousButton.setOnClickListener {
                        iddd--
                        if (iddd == 31) {
                            iddd = 60
                        }
                        displayItem(iddd)
                    }
                }
            }
        }

        setListenersForChip(alsabahChip, almasahChip, 1..30, getString(R.string.azkar_alsabah))
        setListenersForChip(almasahChip, alsabahChip, 31..60, getString(R.string.azkar_almasah))


        shareFloatingButton.setOnClickListener {

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, zikrTextView.text)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, "Share To:"))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher =
                registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.

                        viewModel.onPermissionResult(
                            Manifest.permission.POST_NOTIFICATIONS,
                            isGranted
                        )

                        Log.d("Notification", "Here We go babe!!1")
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // feature requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                        Snackbar.make(
                            view,
                            "The feature is unavailable because the required permission has been denied.",
                            Snackbar.LENGTH_LONG
                        ).show()

                    }
                }
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        return view
    }
}