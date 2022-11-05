package com.example.oceanit.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit2 {

    private val retrofit = Retrofit.Builder()
        .baseUrl("")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(O2_Interface::class.java)

    // 다른 페이지에서 선언해 사용 초기화
    fun getInstance(): O2_Interface?{
        return api
    }
}