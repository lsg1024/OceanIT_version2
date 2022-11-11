package com.example.oceanit

import android.graphics.Color
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
import androidx.fragment.app.findFragment
import com.example.oceanit.DTO.SensorDTO
import com.example.oceanit.DTO.companyDTO
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.github.anastr.speedviewlib.SpeedView
import com.github.anastr.speedviewlib.components.Section
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException


class MainFragment : Fragment() {

    lateinit var speedView1: SpeedView
    lateinit var progressMaxText : TextView
    lateinit var progressMinText : TextView

    lateinit var speedView2: SpeedView
    lateinit var progressMaxText2 : TextView
    lateinit var progressMinText2 : TextView

    lateinit var speedView3: SpeedView
    lateinit var progressMaxText3 : TextView
    lateinit var progressMinText3 : TextView

    lateinit var speedView4: SpeedView
    lateinit var progressMaxText4 : TextView
    lateinit var progressMinText4 : TextView

    lateinit var speedView5: SpeedView
    lateinit var progressMaxText5 : TextView
    lateinit var progressMinText5 : TextView

    lateinit var speedView6: SpeedView
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

        speedView1 = view.findViewById(R.id.speedView1)

        speedView1.clearSections()
        speedView1.addSections(
            Section(0f, .2f, Color.BLUE, speedView1.dpTOpx(30f)),
            Section(.2f, .8f, Color.GREEN, speedView1.dpTOpx(30f)),
            Section(.8f, 1f, Color.RED, speedView1.dpTOpx(30f)),
        )

        progressMaxText = view.findViewById(R.id.max1)
        progressMinText = view.findViewById(R.id.min1)

        speedView2 = view.findViewById(R.id.speedView2)
        speedView2.clearSections()
        speedView2.addSections(
            Section(0f, .2f, Color.BLUE, speedView2.dpTOpx(30f)),
            Section(.2f, .8f, Color.GREEN, speedView2.dpTOpx(30f)),
            Section(.8f, 1f, Color.RED, speedView2.dpTOpx(30f)),
        )

        progressMaxText2 = view.findViewById(R.id.max2)
        progressMinText2 = view.findViewById(R.id.min2)

        speedView3 = view.findViewById(R.id.speedView3)
        speedView3.clearSections()
        speedView3.addSections(
            Section(0f, .2f, Color.BLUE, speedView3.dpTOpx(30f)),
            Section(.2f, .8f, Color.GREEN, speedView3.dpTOpx(30f)),
            Section(.8f, 1f, Color.RED, speedView3.dpTOpx(30f)),
        )

        progressMaxText3 = view.findViewById(R.id.max3)
        progressMinText3 = view.findViewById(R.id.min3)

        speedView4 = view.findViewById(R.id.speedView4)
        speedView4.clearSections()
        speedView4.addSections(
            Section(0f, .2f, Color.BLUE, speedView4.dpTOpx(30f)),
            Section(.2f, .8f, Color.GREEN, speedView4.dpTOpx(30f)),
            Section(.8f, 1f, Color.RED, speedView4.dpTOpx(30f)),
        )
        progressMaxText4 = view.findViewById(R.id.max4)
        progressMinText4 = view.findViewById(R.id.min4)

        speedView5 = view.findViewById(R.id.speedView5)
        speedView5.clearSections()
        speedView5.addSections(
            Section(0f, .2f, Color.BLUE, speedView5.dpTOpx(30f)),
            Section(.2f, .8f, Color.GREEN, speedView5.dpTOpx(30f)),
            Section(.8f, 1f, Color.RED, speedView5.dpTOpx(30f)),
        )

        progressMaxText5 = view.findViewById(R.id.max5)
        progressMinText5 = view.findViewById(R.id.min5)

        speedView6 = view.findViewById(R.id.speedView6)
        speedView6.clearSections()
        speedView6.addSections(
            Section(0f, .2f, Color.BLUE, speedView6.dpTOpx(30f)),
            Section(.2f, .8f, Color.GREEN, speedView6.dpTOpx(30f)),
            Section(.8f, 1f, Color.RED, speedView6.dpTOpx(30f)),
        )
        progressMaxText6 = view.findViewById(R.id.max6)
        progressMinText6 = view.findViewById(R.id.min6)

        mainActivity = context as MainActivity

        user_key = Loginkey.getUserKey(mainActivity).toInt()

        try {
            mSocket = IO.socket("")
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
                speedView1.realSpeedTo(data.Tc)

                speedView2.realSpeedTo(data.DO)

                speedView3.realSpeedTo(data.pH)

                speedView4.realSpeedTo(data.Sa)

                speedView5.realSpeedTo(data.ORP)

                speedView6.realSpeedTo(data.TUR)

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
                    progressMaxText.text = result.result.Tc_high.toString() + "℃"
                    progressMinText.text = result.result.Tc_low.toString() + "℃"

                    // 측정되는 최소값과 최대값
                    speedView1.maxSpeed = result.result.Tc_high + 5
                    speedView1.minSpeed = 0.00f

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText2.text = result.result.DO_high.toString()
                    progressMinText2.text = result.result.DO_low.toString()

                    // 측정되는 최소값과 최대값
                    speedView2.maxSpeed = result.result.DO_high + 5
                    speedView2.minSpeed = 0.00f

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText3.text = result.result.pH_high.toString()
                    progressMinText3.text = result.result.pH_low.toString()

                    // 측정되는 최소값과 최대값
                    speedView3.maxSpeed = result.result.pH_high + 5
                    speedView3.minSpeed = 0.00f

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText4.text = result.result.Sa_high.toString()
                    progressMinText4.text = result.result.Sa_low.toString()

                    // 측정되는 최소값과 최대값
                    speedView4.maxSpeed = result.result.Sa_high + 5
                    speedView4.minSpeed = 0.00f

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText5.text = result.result.ORP_high.toString()
                    progressMinText5.text = result.result.ORP_low.toString()

                    // 측정되는 최소값과 최대값
                    speedView5.maxSpeed = result.result.ORP_high + 50
                    speedView5.minSpeed = result.result.ORP_low - 25

                    // 이걸 수정하면 max min -> 서버에서 받아오기
                    progressMaxText6.text = result.result.TUR_high.toString()
                    progressMinText6.text = result.result.TUR_low.toString()

                    // 측정되는 최소값과 최대값
                    speedView6.maxSpeed = result.result.TUR_high + 5
                    speedView6.minSpeed = 0.00f

                }
            }

            override fun onFailure(call: Call<SensorDTO>, t: Throwable) {
                Log.d("progress", "${t.message}")
            }
        })

    }


}