package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.Zikr
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NightFragment : Fragment() {

    private lateinit var calendarRecTextView: TextView
    private lateinit var zikrAdapter: ZikrAdapter
    private lateinit var recyclerViewZikr: RecyclerView

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

        recyclerViewZikr = view.findViewById(R.id.recycler_view_zikr)
        calendarRecTextView = view.findViewById(R.id.calendarRecTextView)

        recyclerViewZikr.hasFixedSize()
        recyclerViewZikr.layoutManager = LinearLayoutManager(requireContext())

        viewModel.islamicCalendarLiveData.observe(viewLifecycleOwner) {
            calendarRecTextView.text = it
        }

        viewModel.getZikr().asLiveData().observe(viewLifecycleOwner) {
            zikrAdapter = ZikrAdapter(it)
            recyclerViewZikr.adapter = zikrAdapter
            zikrAdapter.notifyDataSetChanged()
        }

        return view
    }

}