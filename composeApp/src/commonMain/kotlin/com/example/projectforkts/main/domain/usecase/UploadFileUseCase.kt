package com.example.projectforkts.main.domain.usecase

import com.example.projectforkts.main.domain.model.UploadFileRequest
import com.example.projectforkts.main.domain.repository.FileUploadRepository

class UploadFileUseCase(private val repository: FileUploadRepository) {
    suspend operator fun invoke(
        owner: String,
        repo: String,
        request: UploadFileRequest
    ): Result<Unit> = repository.uploadFile(owner, repo, request)
}