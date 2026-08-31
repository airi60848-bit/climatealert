package com.example.climatealert.data.remote.dto

import com.google.gson.annotations.SerializedName
data class ForecastResponseDto(
    @SerializedName("current") val currentTemp: Int,
    @SerializedName("safetyMessage") val safetyMessage: String,
    @SerializedName("forecast") val forecast: List<HourlyForecastDto>
)
data class HourlyForecastDto(@SerializedName("time") val time: String, @SerializedName("temp") val temp: Int)

//