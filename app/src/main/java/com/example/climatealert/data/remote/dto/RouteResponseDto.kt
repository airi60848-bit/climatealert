
package com.example.climatealert.data.remote.dto

import com.google.gson.annotations.SerializedName
data class RouteResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("shade_percent") val shadePercent: Int,
    @SerializedName("time_minutes") val timeMinutes: Int,
    @SerializedName("points_earned") val pointsEarned: Int? = null,
    @SerializedName("exposure_warning") val exposureWarning: String? = null,
    @SerializedName("polyline") val polyline: String
)