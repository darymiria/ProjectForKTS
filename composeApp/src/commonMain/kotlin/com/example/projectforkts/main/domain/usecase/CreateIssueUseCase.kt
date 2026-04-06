package com.example.projectforkts.main.domain.usecase

import com.example.projectforkts.main.domain.model.CreateIssueRequest
import com.example.projectforkts.main.domain.repository.IssueRepository

class CreateIssueUseCase(private val repository: IssueRepository) {
    suspend operator fun invoke(
        owner: String,
        repo: String,
        request: CreateIssueRequest
    ): Result<Unit> = repository.createIssue(owner, repo, request)
}