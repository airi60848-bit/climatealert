package com.example.climatealert.data.repository

import com.example.climatealert.data.remote.api.RouteApi
import com.example.climatealert.data.remote.dto.AdvisorRequestDto
import com.example.climatealert.domain.repository.AdvisorRepository
import javax.inject.Inject
import javax.inject.Named

class AdvisorRepositoryImpl @Inject constructor(
    @param:Named("backend") private val api: RouteApi,
) : AdvisorRepository {
    override suspend fun getChatResponse(message: String, lat: Double, lon: Double): String {
        return api.getAdvisorResponse(AdvisorRequestDto(message, lat, lon)).reply
    }
}