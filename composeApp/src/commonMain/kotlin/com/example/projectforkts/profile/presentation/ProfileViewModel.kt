package com.example.projectforkts.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectforkts.AppStorage
import com.example.projectforkts.main.data.GitHubApi
import com.example.projectforkts.main.data.UserResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val appStorage: AppStorage
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private val _logoutEvent = Channel<Unit>(Channel.BUFFERED)
    val logoutEvent = _logoutEvent.receiveAsFlow()

    init { loadProfile() }

    fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val profile = GitHubApi.getUserProfile()
                _state.update { it.copy(isLoading = false, profile = profile) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            appStorage.clearToken()
            _logoutEvent.send(Unit)
        }
    }
}

data class ProfileUiState(
    val profile: UserResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)