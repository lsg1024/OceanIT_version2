package com.example.oceanit.Retrofit


import com.example.oceanit.DTO.*
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface O2_Interface {

    // 로그인 -> 헤더에 넣는걸로
    @POST("api/user/login")
    fun login(
        @Header("token") token : String,
        @Body loginData: LoginData
    ) : Call<LoginDTO>

    // 임계치 서버에 저장된 기존 정보 호출
    @GET("api/sensor/set")
    fun Sensor_OG(
        @Header("user_key") user_key: Int?,
    ) : Call<SensorDTO>

    // 임계치 수정하는 라우터
    @POST("api/sensor/set")
    fun Sensor_CG(
        @Header("user_key") user_key: Int?,
        @Body sensor_body : Sensor_Body
    ) : Call<Sensor_CG_DTO>

    // 양식장 이름 호출
    @GET("api/user/user_info/fishery")
    fun company(
        @Header("user_key") user_key: Int?,
    ) : Call<companyDTO>

    @POST("api/user/logout")
    fun logout(
        @Header("user_key") user_key: Int?,
        @Header("user_token") user_token : String
    ) : Call<logoutDTO>

}