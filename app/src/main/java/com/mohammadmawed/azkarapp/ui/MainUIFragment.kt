package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.Zikr

class MainUIFragment : Fragment() {

    private lateinit var zikrTextView: TextView
    private lateinit var repeatTimeTextView: TextView
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button

    private lateinit var viewModel: ZikrViewModel
    lateinit var zikr: Zikr

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
        previousButton = view.findViewById(R.id.previousButton)

        viewModel = ViewModelProvider(requireActivity())[ZikrViewModel::class.java]

        viewModel.addZikr()

        var idd: Int = 1

        viewModel.itemById(idd).observe(viewLifecycleOwner, Observer {
            for (zikr in it) {
                Log.d("DataStream1", it.toString())
                zikrTextView.text = zikr.text
                repeatTimeTextView.text = zikr.repeat.toString() + "X"
            }

        })

        nextButton.setOnClickListener {
            idd++
            if (idd == 4) {
                idd = 1
            }
                viewModel.itemById(idd).observe(viewLifecycleOwner, Observer {
                    for (zikr in it) {
                        Log.d("DataStream1", it.toString())
                        zikrTextView.text = zikr.text
                        repeatTimeTextView.text = zikr.repeat.toString() + "X"
                    }

                })

        }
        previousButton.setOnClickListener {
            idd--
            if (idd == 0) {
                idd = 3
            }
                viewModel.itemById(idd).observe(viewLifecycleOwner, Observer {
                    for (zikr in it) {
                        Log.d("DataStream1", it.toString())
                        zikrTextView.text = zikr.text
                        repeatTimeTextView.text = zikr.repeat.toString() + "X"
                    }

                })

        }

        return view
    }

}