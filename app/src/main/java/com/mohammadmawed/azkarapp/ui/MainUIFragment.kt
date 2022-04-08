package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.Zikr
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainUIFragment : Fragment() {

    private lateinit var zikrTextView: TextView
    private lateinit var hintTextView: TextView
    private lateinit var repeatTimeTextView: TextView
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var shareFloatingButton: FloatingActionButton
    private lateinit var zikrContainer: RelativeLayout

    private lateinit var viewModel: ZikrViewModel
    lateinit var zikr: Zikr

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
            if (idd == 32) {
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
                idd = 31

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

        }

        return view
    }

}