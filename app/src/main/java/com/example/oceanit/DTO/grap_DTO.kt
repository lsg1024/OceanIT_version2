package com.example.oceanit.DTO

import com.google.gson.annotations.SerializedName

data class grap_DTO (

    @SerializedName("result")
    val result : ArrayList<grap_data>
)

data class grap_data(

    @SerializedName("user_key")
    val user_key : Int,

    @SerializedName("sensor_key")
    val sensor_key : Float,

    @SerializedName("DO")
    val DO : Float,

    @SerializedName("DOper")
    val DOper : Float,

    @SerializedName("pH")
    val pH : Float,

    @SerializedName("Sa")
    val Sa : Float,

    @SerializedName("ORP")
    val ORP : Float,

    @SerializedName("Tc")
    val Tc : Float,

    @SerializedName("TUR")
    val TUR : Float,

    @SerializedName("date")
    val date : String,

)
