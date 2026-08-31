package com.example.climatealert.domain.model

data class WeatherAlert(
    val cityName: String,
    val tempCelsius: Int,
    val weatherDescription: String,
    val humidity: Int
)