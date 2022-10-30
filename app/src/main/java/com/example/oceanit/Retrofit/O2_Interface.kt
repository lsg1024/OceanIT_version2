package com.example.oceanit.Retrofit


import com.example.oceanit.DTO.*
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface O2_Interface {

    @POST("api/user/login")
    fun login(
        @Body loginData: LoginData
    ) : Call<LoginDTO>

    @GET("api/sensor/set")
    fun Sensor_OG(
        @Header("user_key") user_key: Int?,
    ) : Call<SensorDTO>

    @POST("api/sensor/set")
    fun Sensor_CG(
        @Header("user_key") user_key: Int?,
        @Body sensor_body : Sensor_Body
    ) : Call<Sensor_CG_DTO>

}