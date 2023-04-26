package com.example.oceanit.Repository

import androidx.lifecycle.MutableLiveData
import com.example.oceanit.DTO.Sensor_Body
import com.example.oceanit.DTO.Sensor_CG_DTO
import com.example.oceanit.Retrofit.Retrofit2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorChangeRepository {

    private val call by lazy { Retrofit2.getInstance()}

    fun SensorDataChange(user_key:Int, sensorData: Sensor_Body) : MutableLiveData<Sensor_CG_DTO> {
        val data = MutableLiveData<Sensor_CG_DTO>()
        call?.Sensor_CG(user_key, sensorData)!!.enqueue(object : Callback<Sensor_CG_DTO> {
            override fun onResponse(call: Call<Sensor_CG_DTO>, response: Response<Sensor_CG_DTO>) {
                val result = response.body()
                data.value = result!!
            }

            override fun onFailure(call: Call<Sensor_CG_DTO>, t: Throwable) {

            }

        })
        return data
    }
}