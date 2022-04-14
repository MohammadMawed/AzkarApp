package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.Zikr
import com.mohammadmawed.azkarapp.receiver.NotificationBuilder
import com.mohammadmawed.azkarapp.util.NotificationUtils





class MainUIFragment : Fragment() {

    private lateinit var zikrTextView: TextView
    private lateinit var hintTextView: TextView
    private lateinit var repeatTimeTextView: TextView
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var shareFloatingButton: FloatingActionButton
    private lateinit var zikrContainer: RelativeLayout
    private lateinit var nav_menu: BottomNavigationView

    private lateinit var viewModel: ZikrViewModel
    private lateinit var notificationBuilder: NotificationBuilder
    private lateinit var notificationBuilder1: NotificationBuilder

    lateinit var zikr: Zikr

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        repeatTimeTextView = view.findViewById(R.id.repeatTimeTextView)
        nextButton = view.findViewById(R.id.nextButton)
        previousButton = view.findViewById(R.id.previousButton)
        shareFloatingButton = view.findViewById(R.id.shareFloatingButton)
        zikrContainer = view.findViewById(R.id.zikrContainer)


        viewModel = ViewModelProvider(requireActivity())[ZikrViewModel::class.java]
        notificationBuilder = context?.let { NotificationBuilder(it) }!!

        val context = context

        /*val selectedItemId: Int = nav_menu.selectedItemId
        val badge = nav_menu.getOrCreateBadge(selectedItemId)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number is set*/

        viewModel.addZikr()

        var idd: Int = 1

        viewModel.itemById(idd).observe(viewLifecycleOwner, Observer {
            for (zikr in it) {
                zikrTextView.text = zikr.text
                hintTextView.text = zikr.hint
                repeatTimeTextView.text = zikr.repeat.toString() + "X"
            }

            val sss = it.lastIndex

        })

        nextButton.setOnClickListener {
            idd++
            if (idd == 33) {
                idd = 1
            }
                viewModel.itemById(idd).observe(viewLifecycleOwner, Observer {
                    for (zikr in it) {
                        zikrTextView.text = zikr.text
                        hintTextView.text = zikr.hint
                        repeatTimeTextView.text = zikr.repeat.toString() + "X"

                    }

                })

        }
        previousButton.setOnClickListener {
            idd--
            if (idd == 0) {
                idd = 32

            }
                viewModel.itemById(idd).observe(viewLifecycleOwner, Observer {
                    for (zikr in it) {
                        zikrTextView.text = zikr.text
                        hintTextView.text = zikr.hint
                        repeatTimeTextView.text = zikr.repeat.toString() + "X"
                    }

                })
        }
        shareFloatingButton.setOnClickListener {

            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, zikrTextView.text)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, zikrTextView.text))


            context?.let {
                NotificationManagerCompat.from(it).apply {
                    this.notify(1, notificationBuilder.repliedNotification)
                }
            }
        }

        createChannel(
            "CHANNEL_ID",
            getString(R.string.notification_channel_name)
        )

        context?.let { viewModel.reminderNotification(it) }


        return view

    }
    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )// TODO: Step 2.6 disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_content)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

}