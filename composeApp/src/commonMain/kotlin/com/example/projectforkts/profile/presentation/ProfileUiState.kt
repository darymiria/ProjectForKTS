package com.example.projectforkts.profile.presentation


import com.example.projectforkts.profile.domain.UserProfile

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
