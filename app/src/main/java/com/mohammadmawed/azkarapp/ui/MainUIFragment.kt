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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.PreferencesManager
import com.mohammadmawed.azkarapp.data.Zikr
import kotlinx.coroutines.launch


class MainUIFragment : Fragment() {

    private lateinit var zikrTextView: TextView
    private lateinit var hintTextView: TextView
    private lateinit var repeatTimeTextView: TextView
    private lateinit var calendarTextView: TextView
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var shareFloatingButton: FloatingActionButton
    private lateinit var zikrContainer: RelativeLayout
    private lateinit var nav_menu: BottomNavigationView

    private lateinit var viewModel: ZikrViewModel
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
        repeatTimeTextView = view.findViewById(R.id.repeatTimeTextView)
        calendarTextView = view.findViewById(R.id.calendarTextView)
        nextButton = view.findViewById(R.id.nextButton)
        previousButton = view.findViewById(R.id.previousButton)
        shareFloatingButton = view.findViewById(R.id.shareFloatingButton)
        zikrContainer = view.findViewById(R.id.zikrContainer)


        viewModel = ViewModelProvider(requireActivity())[ZikrViewModel::class.java]


        val context = context

        /*val selectedItemId: Int = nav_menu.selectedItemId
        val badge = nav_menu.getOrCreateBadge(selectedItemId)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number is set*/

        createChannel()

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
                type = "text/plain"
                val text: String = zikrTextView.text.toString()
                putExtra(Intent.EXTRA_STREAM, text)
            }
            startActivity(Intent.createChooser(shareIntent, "Share using"))

        }

        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner, Observer {
            calendarTextView.text = it
        })



        return view

    }

    fun createChannel() {
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
