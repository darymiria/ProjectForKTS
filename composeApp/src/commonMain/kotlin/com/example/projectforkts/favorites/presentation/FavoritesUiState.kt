package com.example.projectforkts.favorites.presentation

import com.example.projectforkts.main.domain.model.RepoItem

data class FavoritesUiState(
    val items: List<RepoItem> = emptyList()
)
