package com.example.projectforkts.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class LoginUiEvent {
    data object LoginSuccess : LoginUiEvent()
}

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<LoginUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<LoginUiEvent> = _events.asSharedFlow()

    fun onUsernameChanged(newValue: String) {
        _state.update {
            val newUsername = newValue
            it.copy(
                username = newUsername,
                error = null,
                isLoginButtonActive = newUsername.isNotBlank() && it.password.isNotBlank()
            )
        }
    }

    fun onPasswordChanged(newValue: String) {
        _state.update {
            it.copy(
                password = newValue,
                error = null,
                isLoginButtonActive = it.username.isNotBlank() && newValue.isNotBlank()
            )
        }
    }

    fun onLoginClick() {
        val current = _state.value
        if (current.username == "admin" && current.password == "admin") {
            _events.tryEmit(LoginUiEvent.LoginSuccess)
        } else {
            _state.update { it.copy(error = "error_invalid_credentials") }
        }
    }
}


