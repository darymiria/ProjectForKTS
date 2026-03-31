package com.example.projectforkts.main.domain.usecase

import com.example.projectforkts.main.domain.model.FileItem
import com.example.projectforkts.main.domain.repository.RepoDetailsRepository

class GetFilesUseCase(private val repository: RepoDetailsRepository) {
    suspend operator fun invoke(owner: String, repo: String, path: String = ""): Result<List<FileItem>> = repository. getFiles(owner, repo, path)
}