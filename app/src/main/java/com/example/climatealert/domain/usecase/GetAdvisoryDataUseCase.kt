package com.example.climatealert.domain.usecase


import com.example.climatealert.domain.model.AdvisoryData
import com.example.climatealert.domain.repository.AdvisoryRepository
import javax.inject.Inject
class GetAdvisoryDataUseCase @Inject constructor(private val repo: AdvisoryRepository) {
    suspend operator fun invoke(lat: Double, lon: Double): AdvisoryData = repo.getAdvisoryData(lat, lon)
}