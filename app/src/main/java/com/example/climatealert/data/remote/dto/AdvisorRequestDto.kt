package com.example.climatealert.data.remote.dto

import com.google.gson.annotations.SerializedName
data class AdvisorRequestDto(@SerializedName("message") val message: String, @SerializedName("lat") val lat: Double, @SerializedName("lon") val lon: Double)
