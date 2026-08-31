package com.example.climatealert.presentation.ui.advisory


import com.example.climatealert.domain.model.AdvisoryData
sealed class AdvisoryUiState {
    object Loading : AdvisoryUiState()
    data class Success(val data: AdvisoryData) : AdvisoryUiState()
    data class Error(val message: String) : AdvisoryUiState()
}
