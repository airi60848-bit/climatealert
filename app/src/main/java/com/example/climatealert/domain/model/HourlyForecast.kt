package com.example.climatealert.domain.model


data class AdvisoryData(
    val currentTemp: Int,
    val safetyMessage: String,
    val forecast: List<HourlyForecast>,
    val nearestCenter: CoolingCenter?
)