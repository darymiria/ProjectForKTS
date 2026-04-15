package com.example.projectforkts.main.domain.repository

import com.example.projectforkts.main.domain.model.CreateIssueRequest

interface IssueRepository {
    suspend fun createIssue(
        owner: String,
        repo: String,
        request: CreateIssueRequest
    ): Result<Unit>
}