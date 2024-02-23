package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
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


        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner) {
            calendarTextView.text = it
        }

        var idd = 1

        /*val selectedItemId: Int = nav_menu.selectedItemId
        val badge = nav_menu.getOrCreateBadge(selectedItemId)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number is set*/

        createChannel()

        viewModel.itemById(idd, false).asLiveData().observe(viewLifecycleOwner) { list ->
            for (zikr in list) {
                zikrTextView.text = zikr.text
                hintTextView.text = zikr.hint
                repeatTimeTextView.text = zikr.repeat.toString() + "X"
                indexTextView.text = "$idd/30"
            }

        }

        nextButton.setOnClickListener {
            idd++
            if (idd == 31) {
                idd = 1
            }
            viewModel.itemById(idd, false).asLiveData().observe(viewLifecycleOwner) {
                for (zikr in it) {
                    zikrTextView.text = zikr.text
                    hintTextView.text = zikr.hint
                    repeatTimeTextView.text = zikr.repeat.toString() + "X"
                    indexTextView.text = "$idd/30"
                }
            }
        }
        previousButton.setOnClickListener {
            idd--
            if (idd == 0) {
                idd = 30

            }
            viewModel.itemById(idd, false).asLiveData().observe(viewLifecycleOwner) {
                for (zikr in it) {
                    zikrTextView.text = zikr.text
                    hintTextView.text = zikr.hint
                    repeatTimeTextView.text = zikr.repeat.toString() + "X"
                    indexTextView.text = "$idd/30"
                }
            }
        }

        alsabahChip.setOnCheckedChangeListener { _, isChecked ->
            almasahChip.isChecked = false
            navigationTextView.text = getString(R.string.azkar_alsabah)
            // Responds to chip checked/unchecked
            if (isChecked) {
                viewModel.itemById(idd, false).asLiveData().observe(viewLifecycleOwner) { list ->
                    for (zikr in list) {
                        zikrTextView.text = zikr.text
                        hintTextView.text = zikr.hint
                        repeatTimeTextView.text = zikr.repeat.toString() + "X"
                        indexTextView.text = "$idd/30"
                    }

                }

                nextButton.setOnClickListener {
                    idd++
                    if (idd == 31) {
                        idd = 1
                    }
                    viewModel.itemById(idd, false).asLiveData().observe(viewLifecycleOwner) {
                        for (zikr in it) {
                            zikrTextView.text = zikr.text
                            hintTextView.text = zikr.hint
                            repeatTimeTextView.text = zikr.repeat.toString() + "X"
                            indexTextView.text = "$idd/30"
                        }

                    }

                }
                previousButton.setOnClickListener {
                    idd--
                    if (idd == 0) {
                        idd = 30

                    }
                    viewModel.itemById(idd, false).asLiveData().observe(viewLifecycleOwner) {
                        for (zikr in it) {
                            zikrTextView.text = zikr.text
                            hintTextView.text = zikr.hint
                            repeatTimeTextView.text = zikr.repeat.toString() + "X"
                            indexTextView.text = "$idd/30"
                        }

                    }
                }
            }
        }
        almasahChip.setOnCheckedChangeListener { _, isChecked ->
            var iddd = 31

            if (isChecked) {
                alsabahChip.isChecked = false

                navigationTextView.text = getString(R.string.azkar_almasah)

                viewModel.getAlmasahZikr(iddd, true).asLiveData()
                    .observe(viewLifecycleOwner) { list ->
                        for (zikr in list) {
                            zikrTextView.text = zikr.text
                            hintTextView.text = zikr.hint
                            repeatTimeTextView.text = zikr.repeat.toString() + "X"
                            val index = iddd - 30
                            indexTextView.text = "$index/30"
                        }

                    }

                nextButton.setOnClickListener {
                    iddd++
                    if (iddd == 61) {
                        iddd = 31
                    }
                    viewModel.getAlmasahZikr(iddd, true).asLiveData().observe(viewLifecycleOwner) {
                        for (zikr in it) {
                            zikrTextView.text = zikr.text
                            hintTextView.text = zikr.hint
                            repeatTimeTextView.text = zikr.repeat.toString() + "X"
                            val index = iddd - 30
                            indexTextView.text = "$index/30"
                        }

                    }

                }
                previousButton.setOnClickListener {
                    iddd--
                    if (iddd == 31) {
                        iddd = 60

                    }
                    viewModel.getAlmasahZikr(iddd, true).asLiveData().observe(viewLifecycleOwner) {
                        for (zikr in it) {
                            zikrTextView.text = zikr.text
                            hintTextView.text = zikr.hint
                            repeatTimeTextView.text = zikr.repeat.toString() + "X"
                            val index = iddd - 30
                            indexTextView.text = "$index/30"
                        }
                    }
                }
            }
        }

        shareFloatingButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, (zikrTextView.text))
            intent.type = "text/plain"
            it.context.startActivity(Intent.createChooser(intent, resources.getString(R.string.send_to)))
        }
        return view
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getText(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel =
                NotificationChannel(getString(R.string.notification_channel_id), name, importance)
            mChannel.description = resources.getString(R.string.notification_content)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}