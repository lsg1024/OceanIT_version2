package com.example.oceanit.Socket_File


data class Sensor_data(

    var sensor_key : Int,
    var user_key : Int,
    var Tc : Float,
    var DO : Float,
    var DOper : Float,
    var pH: Float,
    var Sa: Float,
    var ORP : Float,
    var TUR: Float,
    var date : String

)