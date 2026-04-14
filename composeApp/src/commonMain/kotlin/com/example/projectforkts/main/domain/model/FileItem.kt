package com.example.projectforkts.main.domain.model

data class FileItem(
    val name: String,
    val path: String,
    val type: String,
    val downloadUrl: String?
)
