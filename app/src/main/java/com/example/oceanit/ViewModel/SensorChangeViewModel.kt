package com.example.oceanit.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.oceanit.DTO.Sensor_Body
import com.example.oceanit.DTO.Sensor_CG_DTO
import com.example.oceanit.Repository.SensorChangeRepository
import com.example.oceanit.Retrofit.Loginkey

class SensorChangeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SensorChangeRepository()

    fun sendSensorData(user_key: Int, sensorData: Sensor_Body) : MutableLiveData<Sensor_CG_DTO>{
        return repository.SensorDataChange(user_key, sensorData)
    }
}