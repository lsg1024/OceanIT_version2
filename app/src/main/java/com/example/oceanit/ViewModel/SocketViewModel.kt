package com.example.oceanit.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.oceanit.Repository.SocketRepository
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.*
import java.net.URISyntaxException

class SocketViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SocketRepository()

    val sensorData: MutableLiveData<Sensor_data?> = repository.sensorData
    val sensorBeforeData: MutableLiveData<Array<Sensor_data>?> = repository.sensorBeforeData

    private val user_key = Loginkey.getUserKey(application).toInt()

//    소켓 호출 초기화
    init {
        repository.initSocket(user_key)
    }

//    연결 해제
    override fun onCleared() {
        super.onCleared()
        repository.disconnectSocket()
    }

}
