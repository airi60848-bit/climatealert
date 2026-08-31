package com.example.climatealert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FortyGuardWeatherDto(
    @SerializedName("temperature") val temperature: Int,
    @SerializedName("source") val source: String,
    @SerializedName("activity_id") val activityId: String? = null
)