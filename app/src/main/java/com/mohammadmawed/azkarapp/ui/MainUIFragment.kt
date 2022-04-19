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
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    private lateinit var calendarTextView: TextView
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var shareFloatingButton: FloatingActionButton
    private lateinit var zikrContainer: RelativeLayout
    private lateinit var nav_menu: BottomNavigationView

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

        zikrTextView = view.findViewById(R.id.zikrTextViewNight)
        hintTextView = view.findViewById(R.id.hintTextViewNight)
        indexTextView = view.findViewById(R.id.indexTextViewNight)
        repeatTimeTextView = view.findViewById(R.id.repeatTimeTextViewNight)
        calendarTextView = view.findViewById(R.id.calendarTextViewNight)
        nextButton = view.findViewById(R.id.nextButtonNight)
        previousButton = view.findViewById(R.id.previousButtonNight)
        shareFloatingButton = view.findViewById(R.id.shareFloatingButtonNight)
        zikrContainer = view.findViewById(R.id.zikrContainerNight)


        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner) {
            calendarTextView.text = it
        }

        val context = context

        /*val selectedItemId: Int = nav_menu.selectedItemId
        val badge = nav_menu.getOrCreateBadge(selectedItemId)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number is set*/

        createChannel()


        var idd: Int = 1

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
            viewModel.itemById(idd, false).asLiveData().observe(viewLifecycleOwner, Observer {
                for (zikr in it) {
                    zikrTextView.text = zikr.text
                    hintTextView.text = zikr.hint
                    repeatTimeTextView.text = zikr.repeat.toString() + "X"
                    indexTextView.text = "$idd/30"
                }

            })
        }
        shareFloatingButton.setOnClickListener {

            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                val text: String = zikrTextView.text.toString()
                putExtra(Intent.EXTRA_STREAM, text)
            }
            startActivity(Intent.createChooser(shareIntent, "Share using"))

        }

        return view
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel
            val name = getText(R.string.notification_channel_name)
            val descriptionText = "Zikr"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel =
                NotificationChannel(getString(R.string.notification_channel_id), name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}
