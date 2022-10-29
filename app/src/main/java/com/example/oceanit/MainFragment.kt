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
import com.example.oceanit.DTO.SensorDTO
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import io.socket.client.IO
import io.socket.client.Socket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException

class MainFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var progress_bar1 : ProgressBar
    lateinit var progressTextView1: TextView
    lateinit var progressMaxText : TextView
    lateinit var progressMinText : TextView
    lateinit var mSocket: Socket
    private var user_key : Int = 0

    val call by lazy { Retrofit2.getInstance() }

    lateinit var mainActivity: MainActivity

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

        mainActivity = context as MainActivity

        user_key = Loginkey.getUserKey(mainActivity).toInt()

        if (Loginkey.getUserKey(mainActivity).isNullOrEmpty()){
            Log.d("Login_key", "Login_key 없음")
        } else {
            Log.d("Login_key", "응답 ${Loginkey.getUserKey(mainActivity)} SettingFragment")

            try {
                mSocket = IO.socket("http://oceanit.synology.me:8001/")

            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }

            call?.Sensor_OG(user_key)?.enqueue(object : Callback<SensorDTO>{

                override fun onResponse(call: Call<SensorDTO>, response: Response<SensorDTO>) {
                    if (response.isSuccessful) {
                        val result : SensorDTO? = response.body()


                        // 소켓에서 받은 데이터를 넣는 곳
                        val data1 = 25.04
                        progress_bar1.progress = data1.toInt()
                        progressTextView1.text = "$data1"

                        // 이걸 수정하면 max min -> 서버에서 받아오기
                        progressMaxText.text = result!!.result.DO_high.toString()
                        progressMinText.text = result.result.DO_low.toString()

                        // 측정되는 최소값과 최대값
                        progress_bar1.max = 50
                        progress_bar1.min = 0

                    }
                }

                override fun onFailure(call: Call<SensorDTO>, t: Throwable) {

                }

            })

        }
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