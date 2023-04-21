package com.example.oceanit.Repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oceanit.DTO.SensorDTO
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException

class SocketRepository() {

    private lateinit var mSocket: Socket
    private val gson = Gson()

    private val _sensorData = MutableLiveData<Sensor_data?>()
    val sensorData: MutableLiveData<Sensor_data?> = _sensorData

    private val _sensorBeforeData = MutableLiveData<Array<Sensor_data>?>()
    val sensorBeforeData: MutableLiveData<Array<Sensor_data>?> = _sensorBeforeData

    private val retrofit = Retrofit2.getInstance()
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val call = Retrofit2.getInstance()

    private val _sensorOG = MutableLiveData<SensorDTO?>()
    val sensorOG: MutableLiveData<SensorDTO?> = _sensorOG

    fun initSocket(user_key : Int) {
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

        observeSocketDataBefore(user_key)
        observeSocketData(user_key)
    }

    fun observeSocketData(user_key: Int) {

        Log.d("observeSocketData()", "observeSocketData Start")

        ioScope.launch {
            withContext(Dispatchers.IO) {
                Log.d("mSocket", "mSocket_mSocket")
                mSocket.on("sensor_update") { args ->

                    val receivedData = (args[0].toString())
                    val parsedData = gson.fromJson(receivedData, Sensor_data::class.java)

                    _sensorData.postValue(parsedData)
                    getSensorOG(user_key)

                    Log.d("observeSocketData()", "observeSocketData")
                    Log.d("parseData", "$parsedData")

                }

            }
        }
    }

    //    이전 공백을 채우기 위한 단발성 호출
    fun observeSocketDataBefore(user_key:Int) {

        Log.d("DataBefore()", "observeSocketDataBefore")
        ioScope.launch {
            Log.d("DataBefore()", "observeSocketDataBefore2")
            withContext(Dispatchers.IO) {
                mSocket.on("sensor_before") { arg ->
                    val receivedData = (arg[0].toString())
                    val parsedData = gson.fromJson(receivedData, Array<Sensor_data>::class.java)

                    _sensorBeforeData.postValue(parsedData)
                    getSensorOG(user_key)

                    mSocket.off("sensor_before")

                }
            }
        }
    }

    fun getSensorOG(user_key: Int) {
        call?.Sensor_OG(user_key)?.enqueue(object : Callback<SensorDTO> {
            override fun onResponse(call: Call<SensorDTO>, response: Response<SensorDTO>) {
                if (response.isSuccessful) {
                    _sensorOG.value = response.body()
                }
            }

            override fun onFailure(call: Call<SensorDTO>, t: Throwable) {

            }
        })
    }

    // 연결 해제
    fun disconnectSocket() {
        mSocket.disconnect()
    }
}
