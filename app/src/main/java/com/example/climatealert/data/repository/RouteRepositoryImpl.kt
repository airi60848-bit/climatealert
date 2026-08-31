package com.example.climatealert.data.repository

import com.example.climatealert.data.remote.api.RouteApi
import com.example.climatealert.data.remote.dto.RouteRequestDto
import com.example.climatealert.data.util.PolylineDecoder
import com.example.climatealert.domain.model.RouteOption
import com.example.climatealert.domain.repository.RouteRepository
import javax.inject.Inject
import javax.inject.Named

// Example for RouteRepositoryImpl.kt
class RouteRepositoryImpl @Inject constructor(
    @param:Named("backend") private val api: RouteApi,
) : RouteRepository {

    override suspend fun getShadedRoutes(startLat: Double, startLon: Double, endLat: Double, endLon: Double): List<RouteOption> {
        val res = api.getShadedRoutes(RouteRequestDto(startLat, startLon, endLat, endLon))
        return res.map { 
            RouteOption(
                it.id, 
                it.name, 
                it.shadePercent, 
                it.timeMinutes, 
                it.pointsEarned, 
                it.exposureWarning, 
                PolylineDecoder.decode(it.polyline)
            ) 
        }
    }
}