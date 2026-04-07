package com.example.projectforkts.main.domain.model

data class UploadFileRequest(
    val path: String,
    val content: ByteArray,
    val commitMessage: String,
)
