package com.example.oceanit.View

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.oceanit.DTO.SensorDTO
import com.example.oceanit.DTO.companyDTO
import com.example.oceanit.R
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.github.anastr.speedviewlib.SpeedView
import com.github.anastr.speedviewlib.components.Section
import com.google.gson.Gson
import com.ramotion.foldingcell.FoldingCell
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException
import java.text.DecimalFormat


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

    lateinit var blink_text1 : TextView
    lateinit var blink_text2 : TextView
    lateinit var blink_text3 : TextView
    lateinit var blink_text4 : TextView
    lateinit var blink_text5 : TextView
    lateinit var blink_text6 : TextView

    lateinit var topText1 : TextView
    lateinit var topText2 : TextView
    lateinit var topText3 : TextView
    lateinit var topText4 : TextView
    lateinit var topText5 : TextView
    lateinit var topText6 : TextView

    lateinit var socket_data: Array<Sensor_data>

    lateinit var fc : FoldingCell
    lateinit var fc2 : FoldingCell
    lateinit var fc3 : FoldingCell
    lateinit var fc4 : FoldingCell
    lateinit var fc5 : FoldingCell
    lateinit var fc6 : FoldingCell
    val df = DecimalFormat("#.##")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        fc = view.findViewById<View>(R.id.folding_cell) as FoldingCell
        fc2 = view.findViewById<View>(R.id.folding_cell2) as FoldingCell
        fc3 = view.findViewById<View>(R.id.folding_cell3) as FoldingCell
        fc4 = view.findViewById<View>(R.id.folding_cell4) as FoldingCell
        fc5 = view.findViewById<View>(R.id.folding_cell5) as FoldingCell
        fc6 = view.findViewById<View>(R.id.folding_cell6) as FoldingCell

        blink_text1= view.findViewById(R.id.blink_text1)
        blink_text2  = view.findViewById(R.id.blink_text2)
        blink_text3 = view.findViewById(R.id.blink_text3)
        blink_text4 = view.findViewById(R.id.blink_text4)
        blink_text5 = view.findViewById(R.id.blink_text5)
        blink_text6 = view.findViewById(R.id.blink_text6)

        topText1 = view.findViewById(R.id.topText1)
        topText2 = view.findViewById(R.id.topText2)
        topText3= view.findViewById(R.id.topText3)
        topText4 = view.findViewById(R.id.topText4)
        topText5 = view.findViewById(R.id.topText5)
        topText6 = view.findViewById(R.id.topText6)

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

        return view

    }

    override fun onStart() {
        super.onStart()

        call?.company(user_key)?.enqueue(object : Callback<companyDTO>{

            override fun onResponse(call: Call<companyDTO>, response: Response<companyDTO>) {
                if (response.isSuccessful) {
                    val result : companyDTO? = response.body()

                    Log.d("company_name", "$result")

                    // ?????? ?????? textViewd??? ????????????
                    main_name.text = result!!.result.company

                }
            }


            override fun onFailure(call: Call<companyDTO>, t: Throwable) {

                Log.d("company_name", "${t.message}")

            }

        })
        // ??????????????? ????????? ????????? ????????????
        call?.Sensor_OG(user_key)?.enqueue(object : Callback<SensorDTO>{

            @SuppressLint("ResourceAsColor")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<SensorDTO>, response: Response<SensorDTO>) {
                if (response.isSuccessful) {
                    val result : SensorDTO? = response.body()

                    // ????????? ????????? ???????????? ??? ???????????? ?????? ???????????? ?????? ?????? ??? ??????
                    Log.d("main_Sensor_OG", "${result!!.result}")
                    var value_number : Float = 0f
                    fun value_num(max : Float, min : Float): Float {
                        value_number = max - min
                        value_number /= 3
                        return value_number
                    }
                    // ?????? ???????????? max min -> ???????????? ????????????
                    progressMaxText.text = result.result.Tc_high.toString() + "???"
                    progressMinText.text = result.result.Tc_low.toString() + "???"

                    value_num(result.result.Tc_high , result.result.Tc_low)
                    // ???????????? ???????????? ?????????
                    speedView1.maxSpeed = result.result.Tc_high + value_number
                    speedView1.minSpeed = result.result.Tc_low - value_number

                    // ?????? ???????????? max min -> ???????????? ????????????
                    progressMaxText2.text = result.result.DO_high.toString()
                    progressMinText2.text = result.result.DO_low.toString()

                    // ???????????? ???????????? ?????????
                    value_num(result.result.DO_high,result.result.DO_low)
                    speedView2.maxSpeed = result.result.DO_high + value_number
                    speedView2.minSpeed = result.result.DO_low - value_number

                    // ?????? ???????????? max min -> ???????????? ????????????
                    progressMaxText3.text = result.result.pH_high.toString()
                    progressMinText3.text = result.result.pH_low.toString()

                    // ???????????? ???????????? ?????????
                    value_num(result.result.pH_high, result.result.pH_low)
                    speedView3.maxSpeed = result.result.pH_high + value_number
                    speedView3.minSpeed = result.result.pH_low - value_number

                    // ?????? ???????????? max min -> ???????????? ????????????
                    progressMaxText4.text = result.result.Sa_high.toString()
                    progressMinText4.text = result.result.Sa_low.toString()

                    // ???????????? ???????????? ?????????
                    value_num(result.result.Sa_high,result.result.Sa_low)
                    speedView4.maxSpeed = result.result.Sa_high + value_number
                    speedView4.minSpeed = result.result.Sa_low - value_number

                    // ?????? ???????????? max min -> ???????????? ????????????
                    progressMaxText5.text = result.result.ORP_high.toString()
                    progressMinText5.text = result.result.ORP_low.toString()

                    // ???????????? ???????????? ?????????
                    value_num(result.result.ORP_high, result.result.ORP_low)
                    speedView5.maxSpeed = result.result.ORP_high + value_number
                    speedView5.minSpeed = result.result.ORP_low - value_number

                    // ?????? ???????????? max min -> ???????????? ????????????
                    progressMaxText6.text = result.result.TUR_high.toString()
                    progressMinText6.text = result.result.TUR_low.toString()

                    // ???????????? ???????????? ?????????
                    value_num(result.result.TUR_high, result.result.TUR_low)
                    speedView6.maxSpeed = result.result.TUR_high + value_number
                    speedView6.minSpeed = result.result.TUR_low - value_number

                    try {
                        mSocket = IO.socket("http://211.184.227.81:8500")
                        Log.d("SOCKET", "Connection success : $mSocket")

                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }


                    mSocket.connect()

                    mSocket.on(Socket.EVENT_CONNECT) { arg: Array<Any?>? ->
                        mSocket.emit("join", gson.toJson(Join_Data(room = user_key)))
                        Log.d("Socket_join", "?????? - before")
                    }



                    mSocket.on("sensor_before", Emitter.Listener { args ->

                        Log.d("Socket_on", "arg before data $args")

                        // ???????????? [{ }] json ????????? array ????????? ???????????????
                        socket_data = gson.fromJson(args[0].toString(), Array<Sensor_data>::class.java)

                        Log.d("Socket_on", "before data ${socket_data.size}")
                        Log.d("Socket_data[0]", "before data ${socket_data[0].Tc}")

                        mainActivity.runOnUiThread(Runnable {
                            // Stuff that updates the UI

                            if (socket_data[0].Tc.toInt() > result.result.Tc_high.toInt()){
                                blink_text1.setBackgroundResource(R.drawable.red_edge)
                            } else if (socket_data[0].Tc.toInt() < result.result.Tc_low.toInt()) {
                                blink_text1.setBackgroundResource(R.drawable.blue_edge)
                            } else {
                                blink_text1.setBackgroundResource(R.drawable.lime_edge)
                            }

                            if (socket_data[0].DO.toInt() > result.result.DO_high.toInt()){
                                blink_text2.setBackgroundResource(R.drawable.red_edge)
                            } else if (socket_data[0].DO.toInt() < result.result.DO_low.toInt()) {
                                blink_text2.setBackgroundResource(R.drawable.blue_edge)
                            } else {
                                blink_text2.setBackgroundResource(R.drawable.lime_edge)
                            }

                            if (socket_data[0].pH.toInt() > result.result.pH_high.toInt()){
                                blink_text3.setBackgroundResource(R.drawable.red_edge)
                            } else if (socket_data[0].pH.toInt() < result.result.pH_low.toInt()) {
                                blink_text3.setBackgroundResource(R.drawable.blue_edge)
                            } else {
                                blink_text3.setBackgroundResource(R.drawable.lime_edge)
                            }

                            if (socket_data[0].Sa.toInt() > result.result.Sa_high.toInt()){
                                blink_text4.setBackgroundResource(R.drawable.red_edge)
                            } else if (socket_data[0].Sa.toInt() < result.result.Sa_low.toInt()) {
                                blink_text4.setBackgroundResource(R.drawable.blue_edge)
                            } else {
                                blink_text4.setBackgroundResource(R.drawable.lime_edge)
                            }

                            if (socket_data[0].ORP.toInt() > result.result.ORP_high.toInt()){
                                blink_text5.setBackgroundResource(R.drawable.red_edge)
                            } else if (socket_data[0].ORP.toInt() < result.result.ORP_low.toInt()) {
                                blink_text5.setBackgroundResource(R.drawable.blue_edge)
                            } else {
                                blink_text5.setBackgroundResource(R.drawable.lime_edge)
                            }

                            if (socket_data[0].TUR.toInt() > result.result.TUR_high.toInt()){
                                blink_text6.setBackgroundResource(R.drawable.red_edge)
                            } else if (socket_data[0].TUR.toInt() < result.result.TUR_low.toInt()) {
                                blink_text6.setBackgroundResource(R.drawable.blue_edge)
                            } else {
                                blink_text6.setBackgroundResource(R.drawable.lime_edge)
                            }

                            topText1.text = socket_data[0].Tc.toString()
                            topText2.text = socket_data[0].DO.toString()
                            topText3.text = socket_data[0].pH.toString()
                            topText4.text = socket_data[0].Sa.toString()
                            topText5.text = socket_data[0].ORP.toString()
                            topText6.text = socket_data[0].TUR.toString()

                            // ???????????? ?????? ???????????? ?????? ???

                            // ???????????? ???????????? 1????????? ????????? ?????? ?????? ?????? ??????????????? ?????? ?????????
                            speedView1.speedTextListener = {df.format(socket_data[0].Tc)}
                            speedView1.realSpeedTo(df.format(socket_data[0].Tc).toFloat())

                            speedView2.speedTextListener = {df.format(socket_data[0].DO)}
                            speedView2.realSpeedTo(df.format(socket_data[0].DO).toFloat())

                            speedView3.speedTextListener = {df.format(socket_data[0].pH)}
                            speedView3.realSpeedTo(df.format(socket_data[0].pH).toFloat())

                            speedView4.speedTextListener = {df.format(socket_data[0].Sa)}
                            speedView4.realSpeedTo(df.format(socket_data[0].Sa).toFloat())

                            speedView5.speedTextListener = {df.format(socket_data[0].ORP)}
                            speedView5.realSpeedTo(df.format(socket_data[0].ORP).toFloat())

                            speedView6.speedTextListener = {df.format(socket_data[0].TUR)}
                            speedView6.realSpeedTo(df.format(socket_data[0].TUR).toFloat())

                            mSocket.close()

                        })

                    })

                }
            }

            override fun onFailure(call: Call<SensorDTO>, t: Throwable) {
                Log.d("progress", "${t.message}")
            }
        })

        fc.setOnClickListener {
            fc.toggle(false)
        }
        fc2.setOnClickListener {
            fc2.toggle(false)
        }
        fc3.setOnClickListener {
            fc3.toggle(false)
        }
        fc4.setOnClickListener {
            fc4.toggle(false)
        }
        fc5.setOnClickListener {
            fc5.toggle(false)
        }
        fc6.setOnClickListener {
            fc6.toggle(false)
        }
    }

    override fun onResume() {
        super.onResume()

        val handler = Handler()
        handler.postDelayed(Runnable {

            call?.Sensor_OG(user_key)?.enqueue(object : Callback<SensorDTO>{

                @SuppressLint("ResourceAsColor")
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<SensorDTO>, response: Response<SensorDTO>) {
                    if (response.isSuccessful) {
                        val result: SensorDTO? = response.body()

                        Log.d("main_Sensor_OG", "${result!!.result}")

                        var value_number = 0f
                        fun value_num(max : Float, min : Float): Float {
                            if (max != 0F && min != 0f){
                                value_number = max - (min*min)/min
                                value_number /= 3
                                Log.d("value_number", "$value_number")
                            } else {
                                value_number = max - (min*min)/1
                                value_number /= 3
                            }

                            return value_number
                        }
                        // ?????? ???????????? max min -> ???????????? ????????????
                        progressMaxText.text = result.result.Tc_high.toString() + "???"
                        progressMinText.text = result.result.Tc_low.toString() + "???"

                        value_num(result.result.Tc_high, result.result.Tc_low)
                        // ???????????? ???????????? ?????????
                        speedView1.maxSpeed = result.result.Tc_high + value_number
                        speedView1.minSpeed = result.result.Tc_low - value_number

                        // ?????? ???????????? max min -> ???????????? ????????????
                        progressMaxText2.text = result.result.DO_high.toString() + "mg/L"
                        progressMinText2.text = result.result.DO_low.toString() + "mg/L"

                        // ???????????? ???????????? ?????????
                        value_num(result.result.DO_high,result.result.DO_low)
                        speedView2.maxSpeed = result.result.DO_high + value_number
                        speedView2.minSpeed = result.result.DO_low - value_number


                        // ?????? ???????????? max min -> ???????????? ????????????
                        progressMaxText3.text = result.result.pH_high.toString() + "ph"
                        progressMinText3.text = result.result.pH_low.toString() + "ph"

                        // ???????????? ???????????? ?????????
                        value_num(result.result.pH_high,result.result.pH_low)
                        speedView3.maxSpeed = result.result.pH_high + value_number
                        speedView3.minSpeed = result.result.pH_low - value_number

                        // ?????? ???????????? max min -> ???????????? ????????????
                        progressMaxText4.text = result.result.Sa_high.toString() + "ppt"
                        progressMinText4.text = result.result.Sa_low.toString() + "ppt"

                        // ???????????? ???????????? ?????????
                        value_num(result.result.Sa_high,result.result.Sa_low)
                        speedView4.maxSpeed = result.result.Sa_high + value_number
                        speedView4.minSpeed = result.result.Sa_low - value_number

                        // ?????? ???????????? max min -> ???????????? ????????????
                        progressMaxText5.text = result.result.ORP_high.toString() + "mV"
                        progressMinText5.text = result.result.ORP_low.toString() + "mV"

                        // ???????????? ???????????? ?????????
                        value_num(result.result.ORP_high,result.result.ORP_low)
                        speedView5.maxSpeed = result.result.ORP_high + value_number
                        speedView5.minSpeed = result.result.ORP_low - value_number

                        // ?????? ???????????? max min -> ???????????? ????????????
                        progressMaxText6.text = result.result.TUR_high.toString() + "NTU"
                        progressMinText6.text = result.result.TUR_low.toString()+ "NTU"

                        // ???????????? ???????????? ?????????
                        value_num(result.result.TUR_high, result.result.TUR_low)
                        speedView6.maxSpeed = result.result.TUR_high + value_number
                        speedView6.minSpeed = result.result.TUR_low - value_number
                    }
                }
                override fun onFailure(call: Call<SensorDTO>, t: Throwable) {
                    Log.d("progress", "${t.message}")
                }
            })

            try {
                mSocket = IO.socket("http://211.184.227.81:8500")
                Log.d("SOCKET", "Connection success : $mSocket")

            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }

            mSocket.connect()

            mSocket.on(Socket.EVENT_CONNECT) { arg: Array<Any?>? ->
                mSocket.emit("join", gson.toJson(Join_Data(room = user_key)))
                Log.d("Socket_join", "?????? - update")
            }

            mSocket.on("sensor_update", Emitter.Listener { args ->

                Log.d("Socket_on", "arg data $args")


                val data = gson.fromJson(args[0].toString(), Sensor_data::class.java)

                Log.d("Socket_on", "gson.fromJson ${data.pH}")

                mainActivity.runOnUiThread(Runnable {
                    // Stuff that updates the UI

                    if (data.Tc.toInt() > speedView1.maxSpeed.toInt() ){
                        blink_text1.setBackgroundResource(R.drawable.red_edge)
                    } else if (data.Tc.toInt() < speedView1.minSpeed.toInt()) {
                        blink_text1.setBackgroundResource(R.drawable.blue_edge)
                    } else {
                        blink_text1.setBackgroundResource(R.drawable.lime_edge)
                    }

                    if (data.DO.toInt() > speedView2.maxSpeed.toInt()){
                        blink_text2.setBackgroundResource(R.drawable.red_edge)
                    } else if (data.DO.toInt() < speedView2.minSpeed.toInt()) {
                        blink_text2.setBackgroundResource(R.drawable.blue_edge)
                    } else {
                        blink_text2.setBackgroundResource(R.drawable.lime_edge)
                    }

                    if (data.pH.toInt() > speedView3.maxSpeed.toInt()){
                        blink_text3.setBackgroundResource(R.drawable.red_edge)
                    } else if (data.pH.toInt() < speedView3.minSpeed.toInt()) {
                        blink_text3.setBackgroundResource(R.drawable.blue_edge)
                    } else {
                        blink_text3.setBackgroundResource(R.drawable.lime_edge)
                    }

                    if (data.Sa.toInt() > speedView4.maxSpeed.toInt()){
                        blink_text4.setBackgroundResource(R.drawable.red_edge)
                    } else if (data.Sa.toInt() < speedView4.minSpeed.toInt()) {
                        blink_text4.setBackgroundResource(R.drawable.blue_edge)
                    } else {
                        blink_text4.setBackgroundResource(R.drawable.lime_edge)
                    }

                    if (data.ORP.toInt() > speedView5.maxSpeed.toInt()){
                        blink_text5.setBackgroundResource(R.drawable.red_edge)
                    } else if (data.ORP.toInt() < speedView5.minSpeed.toInt()) {
                        blink_text5.setBackgroundResource(R.drawable.blue_edge)
                    } else {
                        blink_text5.setBackgroundResource(R.drawable.lime_edge)
                    }

                    if (data.TUR.toInt() > speedView6.maxSpeed.toInt()){
                        blink_text6.setBackgroundResource(R.drawable.red_edge)
                    } else if (data.TUR.toInt() < speedView6.minSpeed.toInt()) {
                        blink_text6.setBackgroundResource(R.drawable.blue_edge)
                    } else {
                        blink_text6.setBackgroundResource(R.drawable.lime_edge)
                    }

                    // ???????????? ?????? ???????????? ?????? ???
                    speedView1.speedTextListener = {df.format(data.Tc)}
                    speedView1.realSpeedTo(data.Tc)

                    speedView2.speedTextListener = {df.format(data.DO)}
                    speedView2.realSpeedTo(data.DO)

                    speedView3.speedTextListener = {df.format(data.pH)}
                    speedView3.realSpeedTo(data.pH)

                    speedView4.speedTextListener = {df.format(data.Sa)}
                    speedView4.realSpeedTo(data.Sa)

                    speedView5.speedTextListener = {df.format(data.ORP)}
                    speedView5.realSpeedTo(data.ORP)

                    speedView6.speedTextListener = {df.format(data.TUR)}
                    speedView6.realSpeedTo(data.TUR)

                    topText1.text = data.Tc.toString()
                    topText2.text = data.DO.toString()
                    topText3.text = data.pH.toString()
                    topText4.text = data.Sa.toString()
                    topText5.text = data.ORP.toString()
                    topText6.text = data.TUR.toString()

                })
            })
        }, 2000)
    }
}