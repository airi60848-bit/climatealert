package com.example.climatealert.domain.repository

import com.example.climatealert.domain.model.WeatherAlert

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherAlert
}