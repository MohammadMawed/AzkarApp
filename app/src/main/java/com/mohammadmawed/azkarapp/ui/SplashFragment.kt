package com.mohammadmawed.azkarapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mohammadmawed.azkarapp.R
import java.util.concurrent.Executors

class SplashFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        // Create an executor that executes tasks in the main thread.
        val mainExecutor = ContextCompat.getMainExecutor(view.context)

        // Execute a task in the main thread
        mainExecutor.execute {
            // You code logic goes here.
            findNavController().navigate(R.id.action_splashFragment_to_mainUIFragment)
        }


        return view
    }

}