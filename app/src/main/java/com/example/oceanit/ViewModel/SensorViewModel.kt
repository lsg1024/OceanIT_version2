package com.example.oceanit.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oceanit.DTO.SensorDTO
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorViewModel(application: Application) : AndroidViewModel(application) {

    private val _sensorOG = MutableLiveData<SensorDTO>()
    val sensorOG: LiveData<SensorDTO> = _sensorOG

    private val retrofit = Retrofit2.getInstance()

    val user_key = Loginkey.getUserKey(application).toInt()

    fun getSensorOG() {
        retrofit?.Sensor_OG(user_key)?.enqueue(object : Callback<SensorDTO>{
            override fun onResponse(call: Call<SensorDTO>, response: Response<SensorDTO>) {
                if (response.isSuccessful) {
                    _sensorOG.value = response.body()
                }
            }

            override fun onFailure(call: Call<SensorDTO>, t: Throwable) {

            }
        })
    }
}