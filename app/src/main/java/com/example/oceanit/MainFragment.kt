package com.example.oceanit

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.oceanit.DTO.SensorDTO
import com.example.oceanit.DTO.companyDTO
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException


class MainFragment : Fragment() {

    lateinit var progress_bar1 : ProgressBar
    lateinit var progressTextView1: TextView
    lateinit var progressMaxText : TextView
    lateinit var progressMinText : TextView

    lateinit var progress_bar2 : ProgressBar
    lateinit var progressTextView2: TextView
    lateinit var progressMaxText2 : TextView
    lateinit var progressMinText2 : TextView

    lateinit var progress_bar3 : ProgressBar
    lateinit var progressTextView3: TextView
    lateinit var progressMaxText3 : TextView
    lateinit var progressMinText3 : TextView

    lateinit var progress_bar4 : ProgressBar
    lateinit var progressTextView4: TextView
    lateinit var progressMaxText4 : TextView
    lateinit var progressMinText4 : TextView

    lateinit var progress_bar5 : ProgressBar
    lateinit var progressTextView5: TextView
    lateinit var progressMaxText5 : TextView
    lateinit var progressMinText5 : TextView

    lateinit var progress_bar6 : ProgressBar
    lateinit var progressTextView6: TextView
    lateinit var progressMaxText6 : TextView
    lateinit var progressMinText6 : TextView
    lateinit var mSocket: Socket
    private var user_key : Int = 0
    private val gson = Gson()
    val call by lazy { Retrofit2.getInstance() }
    lateinit var mainActivity: MainActivity
    lateinit var main_name : TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        main_name = view.findViewById(R.id.main_name)

        progress_bar1 = view.findViewById(R.id.progress_bar1)
        progressTextView1 = view.findViewById(R.id.text_view_progress)
        progressTextView1.bringToFront()
        progressMaxText = view.findViewById(R.id.max1)
        progressMinText = view.findViewById(R.id.min1)

        progress_bar2 = view.findViewById(R.id.progress_bar2)
        progressTextView2 = view.findViewById(R.id.text_view_progress2)
        progressMaxText2 = view.findViewById(R.id.max2)
        progressMinText2 = view.findViewById(R.id.min2)

        progress_bar3 = view.findViewById(R.id.progress_bar3)
        progressTextView3 = view.findViewById(R.id.text_view_progress3)
        progressMaxText3 = view.findViewById(R.id.max3)
        progressMinText3 = view.findViewById(R.id.min3)

        progress_bar4 = view.findViewById(R.id.progress_bar4)
        progressTextView4 = view.findViewById(R.id.text_view_progress4)
        progressMaxText4 = view.findViewById(R.id.max4)
        progressMinText4 = view.findViewById(R.id.min4)

        progress_bar5 = view.findViewById(R.id.progress_bar5)
        progressTextView5 = view.findViewById(R.id.text_view_progress5)
        progressMaxText5 = view.findViewById(R.id.max5)
        progressMinText5 = view.findViewById(R.id.min5)

        progress_bar6 = view.findViewById(R.id.progress_bar6)
        progressTextView6 = view.findViewById(R.id.text_view_progress6)
        progressMaxText6 = view.findViewById(R.id.max6)
        progressMinText6 = view.findViewById(R.id.min6)

        mainActivity = context as MainActivity

        user_key = Loginkey.getUserKey(mainActivity).toInt()

        try {
            mSocket = IO.socket("http://211.184.227.81:8500")
            Log.d("SOCKET", "Connection success : $mSocket")

        } catch (e: URISyntaxException) {
                e.printStackTrace()
        }

        mSocket.connect()

        mSocket.on(Socket.EVENT_CONNECT) { arg: Array<Any?>? ->
            mSocket.emit("join", gson.toJson(Join_Data(room = user_key)))
            Log.d("Socket_join", "입장")
        }

        mSocket.on("sensor_update", Emitter.Listener { args ->

            Log.d("Socket_on", "arg data $args")

            val data = gson.fromJson(args[0].toString(), Sensor_data::class.java)

            Log.d("Socket_on", "gson.fromJson ${data.pH}")


            mainActivity.runOnUiThread(Runnable(){
                // Stuff that updates the UI

                // 소켓에서 받은 데이터를 넣는 곳
                progress_bar1.progress = data.Tc.toInt()
                progressTextView1.text = "${data.Tc}"

                progress_bar2.progress = data.DO.toInt()
                progressTextView2.text = "${data.DO}"

                progress_bar3.progress = data.pH.toInt()
                progressTextView3.text = "${data.pH}"

                progress_bar4.progress = data.Sa.toInt()
                progressTextView4.text = "${data.Sa}"

                progress_bar5.progress = data.ORP.toInt()
                progressTextView5.text = "${data.ORP}"

                progress_bar6.progress = data.TUR.toInt()
                progressTextView6.text = "${data.TUR}"

            })

        })

        return view

    }

    override fun onStart() {
        super.onStart()

        call?.company(user_key)?.enqueue(object : Callback<companyDTO>{

            override fun onResponse(call: Call<companyDTO>, response: Response<companyDTO>) {
                if (response.isSuccessful) {
                    val result : companyDTO? = response.body()

                    Log.d("company_name", "$result")

                    // 회사 이름 textViewd에 넣어주기
                    main_name.text = result!!.result.fishery

                }
            }


            override fun onFailure(call: Call<companyDTO>, t: Throwable) {

                Log.d("company_name", "${t.message}")

            }

        })
        // 사용자에게 보이는 시점에 호출된다
        call?.Sensor_OG(user_key)?.enqueue(object : Callback<SensorDTO>{

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<SensorDTO>, response: Response<SensorDTO>) {
                if (response.isSuccessful) {
                    val result : SensorDTO? = response.body()

                    Log.d("main_Sensor_OG", "${result!!.result}")

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText.text = result!!.result.Tc_high.toString() + "℃"
                    progressMinText.text = result.result.Tc_low.toString() + "℃"

                    // 측정되는 최소값과 최대값
                    progress_bar1.max = result.result.Tc_high.toInt() + 15
                    progress_bar1.min = result.result.Tc_low.toInt() - 15

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText2.text = result.result.DO_high.toString()
                    progressMinText2.text = result.result.DO_low.toString()

                    // 측정되는 최소값과 최대값
                    progress_bar2.max = 40
                    progress_bar2.min = 0

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText3.text = result.result.pH_high.toString()
                    progressMinText3.text = result.result.pH_low.toString()

                    // 측정되는 최소값과 최대값
                    progress_bar3.max = 40
                    progress_bar3.min = 0

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText4.text = result.result.Sa_high.toString()
                    progressMinText4.text = result.result.Sa_low.toString()

                    // 측정되는 최소값과 최대값
                    progress_bar4.max = 40
                    progress_bar4.min = 0

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText5.text = result.result.ORP_high.toString()
                    progressMinText5.text = result.result.ORP_low.toString()

                    // 측정되는 최소값과 최대값
                    progress_bar5.max = 300
                    progress_bar5.min = 0

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText6.text = result.result.TUR_high.toString()
                    progressMinText6.text = result.result.TUR_low.toString()

                    // 측정되는 최소값과 최대값
                    progress_bar6.max = 40
                    progress_bar6.min = 0

                }
            }

            override fun onFailure(call: Call<SensorDTO>, t: Throwable) {
                Log.d("progress", "${t.message}")
            }
        })

    }


}