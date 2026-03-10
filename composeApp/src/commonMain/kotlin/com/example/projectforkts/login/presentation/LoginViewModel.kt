package com.example.projectforkts.login.presentation

import androidx.lifecycle.ViewModel
import com.example.projectforkts.login.presentation.LoginUiState
import com.example.projectforkts.login.data.LoginRepositoryImpl
import com.example.projectforkts.login.domain.LoginRepository
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

private val loginRepository: LoginRepository = LoginRepositoryImpl()
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
        loginRepository.login(current.username, current.password)
            .onSuccess {
                _events.tryEmit(LoginUiEvent.LoginSuccess)
            }
            .onFailure { exception ->
                _state.update { it.copy(error = exception.message) }
            }
    }
}


