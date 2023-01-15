package com.example.oceanit.DTO

import com.google.gson.annotations.SerializedName

data class LoginDTO(

    @SerializedName("result")
    val result : LoginResult
)

data class LoginData(

    @SerializedName("id")
    val id : String,

    @SerializedName("pw")
    val pw : String,

)

data class LoginResult(
    @SerializedName("user_key")
    val user_key : Int?,

    @SerializedName("msg")
    val msg : String
)


