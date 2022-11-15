package com.example.oceanit.DTO

import com.google.gson.annotations.SerializedName

data class companyDTO(
    @SerializedName("result")
    val result: company_name
)

data class company_name(
    @SerializedName("user_key")
    val user_key : Int,
    @SerializedName("company")
    val company : String,
    @SerializedName("addr")
    val addr : String,
    @SerializedName("tel")
    val tel : String,
    @SerializedName("ceo")
    val ceo : String
)