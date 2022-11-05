package com.example.oceanit.DTO

import com.google.gson.annotations.SerializedName

data class companyDTO(
    @SerializedName("result")
    val result: company_name
)

data class company_name(
    @SerializedName("fishery")
    val fishery : String
)