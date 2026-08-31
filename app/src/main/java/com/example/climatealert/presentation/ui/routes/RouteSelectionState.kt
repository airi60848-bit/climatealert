package com.example.climatealert.presentation.ui.routes


import com.example.climatealert.domain.model.RouteOption
sealed class RouteSelectionUiState {
    object Loading : RouteSelectionUiState()
    data class Success(val routes: List<RouteOption>, val selectedRouteId: String? = null) : RouteSelectionUiState()
    data class Error(val message: String) : RouteSelectionUiState()
}