package com.example.projectforkts.main.presentation.detail

import com.example.projectforkts.main.domain.model.FileItem
import com.example.projectforkts.main.domain.model.RepoDetails

data class RepoDetailUiState (
    val details: RepoDetails? = null,
    val readme: String? = null,
    val files: List<FileItem> = emptyList(),
    val isLoading: Boolean = false,
    val isReadmeLoading: Boolean = false,
    val isFilesLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false
)