package com.mohammadmawed.azkarapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.mohammadmawed.azkarapp.R

class MainUIFragment : Fragment() {

    private lateinit var zikrTextView: TextView

    private lateinit var viewModel: ZikrViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_u_i, container, false)

        zikrTextView = view.findViewById(R.id.zikrTextView)

        viewModel = ViewModelProvider(requireActivity())[ZikrViewModel::class.java]





        return view
    }

}