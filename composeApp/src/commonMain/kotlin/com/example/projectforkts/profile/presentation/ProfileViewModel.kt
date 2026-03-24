package com.example.projectforkts.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectforkts.profile.domain.GetProfileUseCase
import com.example.projectforkts.profile.domain.LogoutUseCase
import com.example.projectforkts.profile.presentation.ProfileUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private val _logoutEvent = Channel<Unit>(Channel.BUFFERED)
    val logoutEvent = _logoutEvent.receiveAsFlow()

    init { loadProfile() }

    fun loadProfile() {
        viewModelScope.launch {
            getProfileUseCase()
                .onSuccess { userProfile ->
                    _state.update { it.copy(isLoading = false, profile = userProfile) }
                }
                .onFailure { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _logoutEvent.send(Unit)
        }
    }

}



