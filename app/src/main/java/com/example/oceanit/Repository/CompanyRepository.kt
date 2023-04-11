package com.example.oceanit.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.oceanit.DTO.companyDTO
import com.example.oceanit.Retrofit.Retrofit2
import retrofit2.Call
import retrofit2.Response

class CompanyRepository {

    val call by lazy { Retrofit2.getInstance() }

    private val _companyData = MutableLiveData<companyDTO?>()
    val companyData : MutableLiveData<companyDTO?> = _companyData

    fun companyFormData(user_key : Int) {
        call!!.company(user_key).enqueue(object : retrofit2.Callback<companyDTO>{
            override fun onResponse(call: Call<companyDTO>, response: Response<companyDTO>) {
                val companyData : companyDTO? = response.body()
                _companyData.postValue(companyData)
            }

            override fun onFailure(call: Call<companyDTO>, t: Throwable) {
                Log.d("company_name", "${t.message}")
            }

        })
    }
}