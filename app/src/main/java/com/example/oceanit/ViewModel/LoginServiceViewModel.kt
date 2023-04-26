package com.example.oceanit.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oceanit.DTO.logoutDTO
import com.example.oceanit.Repository.LoginServiceRepository
import com.example.oceanit.Retrofit.Loginkey

class LoginServiceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LoginServiceRepository(application)
    private val user_key = Loginkey.getUserKey(application).toInt()
    private val user_token = Loginkey.getTokenKey(application)

    val logoutResponse: MutableLiveData<logoutDTO?> = repository.logoutResponse

    init {
        repository.logout(user_key, user_token)
    }

}