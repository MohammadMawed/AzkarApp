package com.mohammadmawed.azkarapp.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.Zikr
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NightFragment : Fragment() {

    private lateinit var zikrTextViewNight: TextView
    private lateinit var hintTextViewNight: TextView
    private lateinit var indexTextViewNight: TextView
    private lateinit var repeatTimeTextViewNight: TextView
    private lateinit var calendarTextViewNight: TextView
    private lateinit var nextButtonNight: Button
    private lateinit var previousButtonNight: Button
    private lateinit var shareFloatingButtonNight: FloatingActionButton
    private lateinit var zikrContainerNight: RelativeLayout

    private val viewModel: ZikrViewModel by viewModels()
    lateinit var zikr: Zikr

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view = inflater.inflate(R.layout.fragment_night, container, false)

        zikrTextViewNight = view.findViewById(R.id.zikrTextViewNight)
        hintTextViewNight = view.findViewById(R.id.hintTextViewNight)
        indexTextViewNight = view.findViewById(R.id.indexTextViewNight)
        repeatTimeTextViewNight = view.findViewById(R.id.repeatTimeTextViewNight)
        calendarTextViewNight = view.findViewById(R.id.calendarTextViewNight)
        nextButtonNight = view.findViewById(R.id.nextButtonNight)
        previousButtonNight = view.findViewById(R.id.previousButtonNight)
        shareFloatingButtonNight = view.findViewById(R.id.shareFloatingButtonNight)
        zikrContainerNight = view.findViewById(R.id.zikrContainerNight)


        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner) {
            calendarTextViewNight.text = it
        }

        var idd: Int = 31

        viewModel.getAlmasahZikr(idd, true).asLiveData().observe(viewLifecycleOwner) { list ->
            for (zikr in list) {
                zikrTextViewNight.text = zikr.text
                hintTextViewNight.text = zikr.hint
                repeatTimeTextViewNight.text = zikr.repeat.toString() + "X"
                val index = idd - 30
                indexTextViewNight.text = "$index/30"
            }

        }



        return view
    }

}