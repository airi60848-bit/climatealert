package com.example.climatealert.presentation.ui.home





import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatealert.data.datasource.LocationDataSource
import com.example.climatealert.domain.usecase.GetWeatherAlertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherAlertUseCase,
    private val locationDataSource: LocationDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        fetchWeather()
    }

    fun fetchWeather() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            val location = locationDataSource.getCurrentLocation()
            if (location == null) {
                _uiState.value = HomeUiState.Error("Unable to get location. Please enable GPS.")
                return@launch
            }

            val (lat, lon) = location
            try {
                val weather = getWeatherUseCase(lat, lon)
                val riskLevel = if (weather.tempCelsius >= 35) "High risk" else "Moderate"
                val warningText = if (weather.tempCelsius >= 35) {
                    "Extreme heat warning\nAvoid outdoor activity until 6 PM."
                } else {
                    "Stay safe and hydrated. ${weather.weatherDescription}"
                }

                _uiState.value = HomeUiState.Success(
                    location = weather.cityName,
                    temperature = weather.tempCelsius,
                    riskLevel = riskLevel,
                    warningText = warningText
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Failed to load weather: ${e.localizedMessage}")
            }
        }
    }
}