package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
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

    @SuppressLint("SetTextI18n")
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

        var iddF: Int = 31

        viewModel.getAlmasahZikr(iddF, true).asLiveData().observe(viewLifecycleOwner) { list ->
            for (zikr in list) {
                zikrTextViewNight.text = zikr.text
                hintTextViewNight.text = zikr.hint
                repeatTimeTextViewNight.text = zikr.repeat.toString() + "X"
                val index = iddF - 30
                indexTextViewNight.text = "$index/30"
            }

        }
        nextButtonNight.setOnClickListener {
            iddF++
            if (iddF == 61) {
                iddF = 31
            }
            viewModel.itemById(iddF, true).asLiveData().observe(viewLifecycleOwner) {
                for (zikr in it) {
                    zikrTextViewNight.text = zikr.text
                    hintTextViewNight.text = zikr.hint
                    repeatTimeTextViewNight.text = zikr.repeat.toString() + "X"
                    val index = iddF - 30
                    indexTextViewNight.text = "$index/30"
                }

            }

        }
        previousButtonNight.setOnClickListener {
            iddF--
            if (iddF == 31) {
                iddF = 60

            }
            viewModel.itemById(iddF, true).asLiveData().observe(viewLifecycleOwner) {
                for (zikr in it) {
                    zikrTextViewNight.text = zikr.text
                    hintTextViewNight.text = zikr.hint
                    repeatTimeTextViewNight.text = zikr.repeat.toString() + "X"
                    val index = iddF - 30
                    indexTextViewNight.text = "$index/30"
                }

            }
        }

        return view
    }

}