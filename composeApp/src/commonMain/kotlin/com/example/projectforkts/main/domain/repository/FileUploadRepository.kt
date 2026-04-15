package com.example.projectforkts.main.domain.repository

import com.example.projectforkts.main.domain.model.UploadFileRequest

interface FileUploadRepository {
    suspend fun uploadFile(
        owner: String,
        repo: String,
        request: UploadFileRequest
    ): Result<Unit>
}