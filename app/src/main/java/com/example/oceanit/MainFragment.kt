package com.example.oceanit

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import at.grabner.circleprogress.CircleProgressView
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class MainFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var progress_bar1 : ProgressBar
    lateinit var progressTextView1: TextView
    lateinit var progressMaxText : TextView
    lateinit var progressMinText : TextView
    lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        progress_bar1 = view.findViewById(R.id.progress_bar1)
        progressTextView1 = view.findViewById(R.id.text_view_progress)
        progressMaxText = view.findViewById(R.id.max1)
        progressMinText = view.findViewById(R.id.min1)

        try {
            mSocket = IO.socket("http://oceanit.synology.me:8001/")

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

//        circleProgressView1.maxValue = 50f
//        circleProgressView1.unit = 20f.toString()
        val data1 = 25.04
        progress_bar1.progress = data1.toInt()
        progressTextView1.text = "$data1"

        // 이걸 수정하면 max min
        progressMaxText.text = "35"
        progressMinText.text = "25"

        val maxValue = (progressMaxText.text as String).toInt()

        Log.d("maxValue", "$maxValue")

        // 측정되는 최소값과 최대값
        progress_bar1.max = maxValue + 10
        progress_bar1.min = 0



        return view
    }

    override fun onStart() {
        super.onStart()


    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}