package com.example.climatealert.presentation.ui.routes



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatealert.domain.usecase.GetCoolestRoutesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteSelectionViewModel @Inject constructor(
    private val getRoutesUseCase: GetCoolestRoutesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RouteSelectionUiState>(RouteSelectionUiState.Loading)
    val uiState: StateFlow<RouteSelectionUiState> = _uiState

    fun fetchRoutes(startLat: Double, startLon: Double, endLat: Double, endLon: Double) {
        viewModelScope.launch {
            _uiState.value = RouteSelectionUiState.Loading
            try {
                val routes = getRoutesUseCase(startLat, startLon, endLat, endLon)
                _uiState.value = RouteSelectionUiState.Success(
                    routes = routes,
                    selectedRouteId = routes.firstOrNull()?.id
                )
            } catch (e: Exception) {
                _uiState.value = RouteSelectionUiState.Error("Failed to load routes: ${e.localizedMessage}")
            }
        }
    }

    fun selectRoute(routeId: String) {
        val current = _uiState.value
        if (current is RouteSelectionUiState.Success) {
            _uiState.value = current.copy(selectedRouteId = routeId)
        }
    }
}