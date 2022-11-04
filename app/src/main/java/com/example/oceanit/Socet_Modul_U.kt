package com.example.oceanit

import android.util.Log
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

//lateinit var mSocket: Socket
//private val gson = Gson()
//lateinit var mainActivity: MainActivity
//
//object Socet_Modul_U {
//
//    val user_key = Loginkey.getUserKey(mainActivity).toInt()
//
//    fun Socket_Connect() {
//        try {
//            mSocket = IO.socket("http://211.184.227.81:8500")
//            Log.d("SOCKET", "Connection success : $mSocket")
//
//        } catch (e: URISyntaxException) {
//            e.printStackTrace()
//        }
//
//        mSocket.connect()
//
//        mSocket.on(Socket.EVENT_CONNECT) { arg: Array<Any?>? ->
//            mSocket.emit("join", gson.toJson(Join_Data(room = user_key)))
//            Log.d("Socket_join", "입장")
//        }
//
//        mSocket.on("sensor_update", Emitter.Listener { args ->
//
//            Log.d("Socket_on", "arg data $args")
//
//            val data = gson.fromJson(args[0].toString(), Sensor_data::class.java)
//
//            Log.d("Socket_on", "gson.fromJson ${data.pH}")
//
//        })
//
//    }
//}