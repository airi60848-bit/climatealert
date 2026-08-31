package com.example.climatealert.data.repository


import com.example.climatealert.data.remote.api.RouteApi
import com.example.climatealert.domain.model.WeatherAlert
import com.example.climatealert.domain.repository.WeatherRepository
import javax.inject.Inject
import javax.inject.Named

class WeatherRepositoryImpl @Inject constructor(
    @param:Named("backend") private val api: RouteApi,
) : WeatherRepository {
    override suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherAlert {
        return try {
            // 1. Try FortyGuard first (hyperlocal)
            val fg = api.getFortyGuardWeather(lat, lon)
            WeatherAlert(
                cityName = "My Location",
                tempCelsius = fg.temperature,
                weatherDescription = "Hyperlocal data via FortyGuard",
                humidity = 0
            )
        } catch (e: Exception) {
            // 2. Fall back to general forecast via backend
            val forecast = api.getForecast(lat, lon)
            WeatherAlert(
                cityName = "My Location",
                tempCelsius = forecast.currentTemp,
                weatherDescription = if (forecast.currentTemp >= 35) "Extreme Heat" else "Moderate",
                humidity = 0
            )
        }
    }
}





