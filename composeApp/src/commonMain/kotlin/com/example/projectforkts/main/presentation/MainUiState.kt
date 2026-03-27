package com.example.projectforkts.main.presentation

import com.example.projectforkts.main.domain.RepoItem

data class MainUiState(
    val items: List<RepoItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFromCache: Boolean = false,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = true,
    val query: String = "kotlin",
)