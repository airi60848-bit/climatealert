package com.example.climatealert.data.remote.api

import com.example.climatealert.data.remote.dto.*
import retrofit2.http.*

interface RouteApi {
    // Weather
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String = "metric"
    ): WeatherResponseDto
    @GET("api/v1/fortyguard")
    suspend fun getFortyGuardWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("date") date: String? = null,
        @Query("time") time: String? = null
    ): FortyGuardWeatherDto


    // Routes
    @POST("api/v1/routes/shaded")
    suspend fun getShadedRoutes(@Body request: RouteRequestDto): List<RouteResponseDto>

    // Forecast
    @GET("api/v1/forecast")
    suspend fun getForecast(@Query("lat") lat: Double, @Query("lon") lon: Double): ForecastResponseDto

    // Cooling Centers
    @GET("api/v1/cooling-centers")
    suspend fun getCoolingCenters(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("radius") radius: Int = 2000): List<CoolingCenterDto>

    // Advisor
    @POST("api/v1/advisor")
    suspend fun getAdvisorResponse(@Body request: AdvisorRequestDto): AdvisorResponseDto
}