package com.example.climatealert.presentation.ui.advisor



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatealert.data.datasource.LocationDataSource
import com.example.climatealert.domain.model.ChatMessage
import com.example.climatealert.domain.usecase.SendChatMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvisorViewModel @Inject constructor(
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val locationDataSource: LocationDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdvisorUiState(
            messages = listOf(
                ChatMessage("Hi! I'm your heat safety advisor. How can I help you today?", false)
            ),
            isLoading = false
        )
    )
    val uiState: StateFlow<AdvisorUiState> = _uiState

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        // Add user message immediately
        _uiState.update {
            it.copy(
                messages = it.messages + ChatMessage(userText, true),
                isLoading = true
            )
        }

        viewModelScope.launch {
            try {
                val location = locationDataSource.getCurrentLocation()
                val (lat, lon) = location ?: Pair(28.6139, 77.2090) // Fallback to Delhi if GPS unavailable

                val reply = sendChatMessageUseCase(userText, lat, lon)

                _uiState.update {
                    it.copy(
                        messages = it.messages + ChatMessage(reply, false),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        messages = it.messages + ChatMessage(
                            "Sorry, I'm having trouble connecting. Please check your internet and try again.",
                            false
                        ),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun resetChat() {
        _uiState.update {
            it.copy(
                messages = listOf(ChatMessage("Hi! I'm your heat safety advisor. How can I help you today?", false)),
                isLoading = false
            )
        }
    }
}
