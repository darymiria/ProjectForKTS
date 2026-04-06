package com.example.projectforkts.main.presentation.issue


data class CreateIssueUiState(
    val title: String = "",
    val body: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)
