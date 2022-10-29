package com.example.oceanit.DTO

import com.google.gson.annotations.SerializedName

data class LoginDTO(

    @SerializedName("result")
    val result : ArrayList<LoginData>
)

data class LoginData(

    @SerializedName("id")
    val id : String,

    @SerializedName("pw")
    val pw : String,
)
