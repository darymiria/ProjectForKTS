package com.example.projectforkts.main.data

import com.example.projectforkts.main.data.network.CreateIssueRequestBody
import com.example.projectforkts.main.data.network.GitHubApi
import com.example.projectforkts.main.domain.model.CreateIssueRequest
import com.example.projectforkts.main.domain.repository.IssueRepository
import io.ktor.utils.io.CancellationException

class IssueRepositoryImpl (private val api: GitHubApi): IssueRepository {
    override suspend fun createIssue(
        owner: String,
        repo: String,
        request: CreateIssueRequest
    ): Result<Unit> = try{
        api.createIssue(owner, repo, CreateIssueRequestBody(request.title, request.body))
        Result.success(Unit)
    } catch(e: CancellationException){
        throw e
    } catch(e: Exception){
        Result.failure(e)
    }
}