package com.example.projectforkts.presentation.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoginButtonActive: Boolean = false
)