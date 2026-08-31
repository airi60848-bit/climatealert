package com.example.climatealert.domain.usecase


import com.example.climatealert.domain.repository.AdvisorRepository
import javax.inject.Inject
class SendChatMessageUseCase @Inject constructor(private val repo: AdvisorRepository) {
    suspend operator fun invoke(message: String, lat: Double, lon: Double): String =
        repo.getChatResponse(message, lat, lon)
}