package com.example.climatealert.data.repository

import com.example.climatealert.data.remote.api.RouteApi
import com.example.climatealert.domain.model.AdvisoryData
import com.example.climatealert.domain.model.CoolingCenter
import com.example.climatealert.domain.model.HourlyForecast
import com.example.climatealert.domain.repository.AdvisoryRepository
import javax.inject.Inject
import javax.inject.Named

class AdvisoryRepositoryImpl @Inject constructor(
    @param:Named("backend") private val api: RouteApi,
) : AdvisoryRepository {
    override suspend fun getAdvisoryData(lat: Double, lon: Double): AdvisoryData {
        val f = api.getForecast(lat, lon)
        val centers = api.getCoolingCenters(lat, lon)
        val nearest = centers.firstOrNull()?.let { CoolingCenter(it.name, it.walkMinutes, it.lat, it.lon) }
        return AdvisoryData(f.currentTemp, f.safetyMessage, f.forecast.map { HourlyForecast(it.time, it.temp) }, nearest)
    }
}