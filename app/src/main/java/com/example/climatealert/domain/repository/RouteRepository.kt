

// RouteRepository.kt
package com.example.climatealert.domain.repository
import com.example.climatealert.domain.model.RouteOption
interface RouteRepository {
    suspend fun getShadedRoutes(startLat: Double, startLon: Double, endLat: Double, endLon: Double): List<RouteOption>
}



