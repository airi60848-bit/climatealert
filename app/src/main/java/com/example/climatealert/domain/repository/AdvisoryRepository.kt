package com.example.climatealert.domain.repository

import com.example.climatealert.domain.model.AdvisoryData
interface AdvisoryRepository {
    suspend fun getAdvisoryData(lat: Double, lon: Double): AdvisoryData
}