
package com.example.climatealert.domain.usecase
import com.example.climatealert.domain.model.RouteOption
import com.example.climatealert.domain.repository.RouteRepository
import javax.inject.Inject
class GetCoolestRoutesUseCase @Inject constructor(private val repo: RouteRepository) {
    suspend operator fun invoke(startLat: Double, startLon: Double, endLat: Double, endLon: Double): List<RouteOption> =
        repo.getShadedRoutes(startLat, startLon, endLat, endLon)
}