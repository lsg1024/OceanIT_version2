package com.example.oceanit.ViewModel

import android.app.Application
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
    }

}