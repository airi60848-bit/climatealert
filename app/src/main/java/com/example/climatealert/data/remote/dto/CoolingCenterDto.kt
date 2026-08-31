package com.example.climatealert.data.remote.dto

import com.google.gson.annotations.SerializedName
data class CoolingCenterDto(
    @SerializedName("name") val name: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("walkMinutes") val walkMinutes: Int
)