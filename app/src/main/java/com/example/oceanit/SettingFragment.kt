package com.example.oceanit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.slider.RangeSlider

class SettingFragment : Fragment() {

    lateinit var rangeSlider: RangeSlider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {



        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_setting, container, false)

        rangeSlider = view.findViewById(R.id.rangeSlider)

        rangeSlider.valueFrom = 10f

        rangeSlider.valueFrom = 100f

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SettingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}