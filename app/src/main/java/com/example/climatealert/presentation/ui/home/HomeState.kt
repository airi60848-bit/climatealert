package com.example.climatealert.presentation.ui.home

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val location: String, val temperature: Int, val riskLevel: String, val warningText: String) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}





