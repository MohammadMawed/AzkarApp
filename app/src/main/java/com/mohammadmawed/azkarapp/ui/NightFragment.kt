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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var calendarRecTextView: TextView
    private lateinit var nextButtonNight: Button
    private lateinit var previousButtonNight: Button
    private lateinit var shareFloatingButtonNight: FloatingActionButton
    private lateinit var zikrContainerNight: RelativeLayout
    private lateinit var zikrAdapter: ZikrAdapter
    private lateinit var recycler_view_zikr: RecyclerView

    private val viewModel: ZikrViewModel by viewModels()
    lateinit var zikr: Zikr

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_night, container, false)

        recycler_view_zikr = view.findViewById(R.id.recycler_view_zikr)
        calendarRecTextView = view.findViewById(R.id.calendarRecTextView)

        recycler_view_zikr.hasFixedSize()
        recycler_view_zikr.layoutManager = LinearLayoutManager(requireContext())

        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner) {
            calendarRecTextView.text = it
        }

        viewModel.getZikr().asLiveData().observe(viewLifecycleOwner, Observer {
            zikrAdapter = ZikrAdapter(it)
            recycler_view_zikr.adapter = zikrAdapter
            zikrAdapter.notifyDataSetChanged()
        })

        return view
    }

}