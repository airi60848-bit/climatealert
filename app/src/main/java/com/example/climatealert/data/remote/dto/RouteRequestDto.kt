package com.example.climatealert.data.remote.dto
import com.google.gson.annotations.SerializedName
data class RouteRequestDto(
    @SerializedName("start_lat") val startLat: Double,
    @SerializedName("start_lon") val startLon: Double,
    @SerializedName("end_lat") val endLat: Double,
    @SerializedName("end_lon") val endLon: Double,
    @SerializedName("current_time") val currentTime: String = java.time.LocalDateTime.now().toString()
)