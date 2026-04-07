package com.example.projectforkts.main.presentation.upload

data class UploadFileUiState(
    val fileName: String = "",
    val filePath: String = "",
    val commitMessage: String = "",
    val fileBytes: ByteArray? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)