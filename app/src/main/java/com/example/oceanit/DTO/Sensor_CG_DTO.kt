package com.example.oceanit.DTO

import com.google.gson.annotations.SerializedName

data class Sensor_CG_DTO(
    val result : Sensor_Change
)

data class Sensor_Change(

    @SerializedName("user_key")
    val user_key : Int,

    @SerializedName("DO_high")
    val DO_high : Float,

    @SerializedName("DO_low")
    val DO_low : Float,

    @SerializedName("pH_high")
    val pH_high : Float,

    @SerializedName("pH_low")
    val pH_low : Float,

    @SerializedName("Sa_high")
    val Sa_high : Float,

    @SerializedName("Sa_low")
    val Sa_low : Float,

    @SerializedName("ORP_high")
    val ORP_high : Float,

    @SerializedName("ORP_low")
    val ORP_low : Float,

    @SerializedName("Tc_high")
    val Tc_high : Float,

    @SerializedName("Tc_low")
    val Tc_low : Float,

    @SerializedName("TUR_high")
    val TUR_high : Float,

    @SerializedName("TUR_low")
    val TUR_low : Float,
)

data class Sensor_Body(

    @SerializedName("DO_high")
    val DO_high : Float,

    @SerializedName("DO_low")
    val DO_low : Float,

    @SerializedName("pH_high")
    val pH_high : Float,

    @SerializedName("pH_low")
    val pH_low : Float,

    @SerializedName("Sa_high")
    val Sa_high : Float,

    @SerializedName("Sa_low")
    val Sa_low : Float,

    @SerializedName("ORP_high")
    val ORP_high : Float,

    @SerializedName("ORP_low")
    val ORP_low : Float,

    @SerializedName("Tc_high")
    val Tc_high : Float,

    @SerializedName("Tc_low")
    val Tc_low : Float,

    @SerializedName("TUR_high")
    val TUR_high : Float,

    @SerializedName("TUR_low")
    val TUR_low : Float,

    )