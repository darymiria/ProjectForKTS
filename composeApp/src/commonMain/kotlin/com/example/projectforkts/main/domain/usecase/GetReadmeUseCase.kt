package com.example.projectforkts.main.domain.usecase

import com.example.projectforkts.main.domain.repository.RepoDetailsRepository

class GetReadmeUseCase(private val repository: RepoDetailsRepository) {
    suspend operator fun invoke(owner: String, repo: String): Result<String> = repository.getReadme(owner, repo)
}