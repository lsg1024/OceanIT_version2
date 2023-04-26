package com.example.oceanit.Repository

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oceanit.DB.AppDatabase
import com.example.oceanit.DTO.logoutDTO
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.example.oceanit.View.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginServiceRepository(application: Application) {

    private val call by lazy { Retrofit2.getInstance() }
    val db = AppDatabase.getDBInstance(application)

    private val _logoutResponse = MutableLiveData<logoutDTO?>()
    val logoutResponse : MutableLiveData<logoutDTO?> = _logoutResponse

//  로그아웃 요청 -> fcm 데이터 삭제
    fun logout(user_key : Int, user_token : String) {
        call?.logout(user_key, user_token)!!.enqueue(object : Callback<logoutDTO>{
            override fun onResponse(call: Call<logoutDTO>, response: Response<logoutDTO>) {

                if (response.isSuccessful){
                    val result = response.body()

                    _logoutResponse.postValue(result)

                } else {
                    Log.d("logout", "response.isSuccessful.Fail")
                }

            }

            override fun onFailure(call: Call<logoutDTO>, t: Throwable) {
                Log.d("logout_fail", "${t.message}")
            }
        })
    }

//  캐쉬와 로컬 디비에 있는 데이터 제거
    fun removeKey() {

        db!!.UserDao()!!.deleteAllUsers()

    }
}