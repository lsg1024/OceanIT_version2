package com.example.oceanit.ViewModel

import android.app.Application
시import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.oceanit.DTO.companyDTO
import com.example.oceanit.Repository.CompanyRepository
import com.example.oceanit.Retrofit.Loginkey

class CompanyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CompanyRepository()

    val companyData : MutableLiveData<companyDTO?> = repository.companyData

    private val user_key = Loginkey.getUserKey(application).toInt()
    init {
        repository.companyFormData(user_key)
        Log.d("CompanyViewModel", "CompanyViewModel_init")

    }

    override fun onCleared() {
        super.onCleared()
        Log.d("CompanyViewModel", "CompanyViewModel_Clear")
    }

}