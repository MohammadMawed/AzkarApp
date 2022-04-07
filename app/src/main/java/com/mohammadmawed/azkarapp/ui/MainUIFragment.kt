package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohammadmawed.azkarapp.R

class MainUIFragment : Fragment() {

    private lateinit var zikrTextView: TextView
    private lateinit var repeatTimeTextView: TextView
    private lateinit var nextButton: Button

    private lateinit var viewModel: ZikrViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_u_i, container, false)

        zikrTextView = view.findViewById(R.id.zikrTextView)
        repeatTimeTextView = view.findViewById(R.id.repeatTimeTextView)
        nextButton = view.findViewById(R.id.nextButton)

        viewModel = ViewModelProvider(requireActivity())[ZikrViewModel::class.java]

        viewModel.addZikr()

        viewModel.readAllData.observe(viewLifecycleOwner, Observer { zikr->
            zikrTextView.text = zikr.text
            repeatTimeTextView.text = zikr.repeat.toString() + "X"
            nextButton.setOnClickListener {
                zikr.id
            }
        })


        return view
    }

}