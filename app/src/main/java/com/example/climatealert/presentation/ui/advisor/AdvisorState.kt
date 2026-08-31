package com.example.climatealert.presentation.ui.advisor


import com.example.climatealert.domain.model.ChatMessage
data class AdvisorUiState(val messages: List<ChatMessage> = emptyList(), val isLoading: Boolean = false)
