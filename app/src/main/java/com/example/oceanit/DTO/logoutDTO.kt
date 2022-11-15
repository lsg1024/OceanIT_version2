package com.example.oceanit.DTO

import com.google.gson.annotations.SerializedName

data class logoutDTO(
    @SerializedName("result")
    val result : logout_response
)

data class logout_response(
    @SerializedName("user_key")
    val user_key : Int,
    @SerializedName("user_token")
    val user_token : String
)
