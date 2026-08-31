package com.example.climatealert.domain.repository

interface AdvisorRepository {
    suspend fun getChatResponse(message: String, lat: Double, lon: Double): String
}