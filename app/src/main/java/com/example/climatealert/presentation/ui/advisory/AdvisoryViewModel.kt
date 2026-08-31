package com.example.climatealert.presentation.ui.advisory


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatealert.data.datasource.LocationDataSource
import com.example.climatealert.domain.usecase.GetAdvisoryDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvisoryViewModel @Inject constructor(
    private val getAdvisoryDataUseCase: GetAdvisoryDataUseCase,
    private val locationDataSource: LocationDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdvisoryUiState>(AdvisoryUiState.Loading)
    val uiState: StateFlow<AdvisoryUiState> = _uiState

    init {
        fetchAdvisoryData()
    }

    fun fetchAdvisoryData() {
        viewModelScope.launch {
            _uiState.value = AdvisoryUiState.Loading

            val location = locationDataSource.getCurrentLocation()
            if (location == null) {
                _uiState.value = AdvisoryUiState.Error("Unable to get location.")
                return@launch
            }

            val (lat, lon) = location
            try {
                val data = getAdvisoryDataUseCase(lat, lon)
                _uiState.value = AdvisoryUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = AdvisoryUiState.Error(e.localizedMessage ?: "Failed to load advisory data")
            }
        }
    }
}