package com.example.climatealert.domain.usecase

import com.example.climatealert.domain.model.WeatherAlert
import com.example.climatealert.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherAlertUseCase @Inject constructor(private val repo: WeatherRepository) {
    suspend operator fun invoke(lat: Double, lon: Double): WeatherAlert = repo.getCurrentWeather(lat, lon)
}





