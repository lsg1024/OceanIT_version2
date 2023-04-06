package com.example.oceanit.Repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.*
import java.net.URISyntaxException

class SocketRepository() {

    private lateinit var mSocket: Socket
    private val gson = Gson()

    private val _sensorData = MutableLiveData<Sensor_data?>()
    val sensorData: MutableLiveData<Sensor_data?> = _sensorData

    private val _sensorBeforeData = MutableLiveData<Array<Sensor_data>?>()
    val sensorBeforeData: MutableLiveData<Array<Sensor_data>?> = _sensorBeforeData

    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun initSocket(user_key: Int) {
        try {
            mSocket = IO.socket("http://211.184.227.81:8500")
            Log.d("initSocket", "initSocket called")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        mSocket.connect()

        mSocket.on(Socket.EVENT_CONNECT) {
            mSocket.emit("join", gson.toJson(Join_Data(room = user_key)))
            _sensorData.postValue(null)
        }

        observeSocketDataBefore()
        observeSocketData()
    }

    fun observeSocketData() {

        Log.d("observeSocketData()", "observeSocketData Start")

        ioScope.launch {
            withContext(Dispatchers.IO) {
                Log.d("mSocket", "mSocket_mSocket")
                mSocket.on("sensor_update") { args ->

                    val receivedData = (args[0].toString())
                    val parsedData = gson.fromJson(receivedData, Sensor_data::class.java)

                    _sensorData.postValue(parsedData)

                    Log.d("observeSocketData()", "observeSocketData")
                    Log.d("parseData", "$parsedData")

                }

            }
        }
    }

    //    이전 공백을 채우기 위한 단발성 호출
    fun observeSocketDataBefore() {

        Log.d("DataBefore()", "observeSocketDataBefore")
        ioScope.launch {
            Log.d("DataBefore()", "observeSocketDataBefore2")
            withContext(Dispatchers.IO) {
                mSocket.on("sensor_before") { arg ->
                    val receivedData = (arg[0].toString())
                    val parsedData = gson.fromJson(receivedData, Array<Sensor_data>::class.java)

                    _sensorBeforeData.postValue(parsedData)

                    mSocket.off("sensor_before")

                }
            }
        }
    }
    // 연결 해제
    fun disconnectSocket() {
        mSocket.disconnect()
    }
}
