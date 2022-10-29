package com.example.oceanit.Retrofit


import com.example.oceanit.DTO.LoginDTO
import com.example.oceanit.DTO.LoginData
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.POST


interface O2_Interface {

    @POST("api/user/login")
    fun login(
        @Body loginData: LoginData
    ) : Call<LoginDTO>


}