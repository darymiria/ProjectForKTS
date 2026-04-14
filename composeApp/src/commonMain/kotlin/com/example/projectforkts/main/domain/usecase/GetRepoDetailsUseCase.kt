package com.example.projectforkts.main.domain.usecase

import com.example.projectforkts.main.domain.model.RepoDetails
import com.example.projectforkts.main.domain.repository.RepoDetailsRepository

class GetRepoDetailsUseCase(private val repository: RepoDetailsRepository) {
    suspend operator fun invoke(owner: String, repo: String): Result<RepoDetails> = repository.getRepoDetails(owner, repo)
}