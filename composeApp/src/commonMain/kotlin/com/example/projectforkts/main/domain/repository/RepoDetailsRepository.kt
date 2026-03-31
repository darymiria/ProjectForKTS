package com.example.projectforkts.main.domain.repository

import com.example.projectforkts.main.domain.model.FileItem
import com.example.projectforkts.main.domain.model.RepoDetails

interface RepoDetailsRepository {
    suspend fun getRepoDetails(owner: String, repo: String) : Result<RepoDetails>
    suspend fun getReadme(owner: String, repo: String): Result<String>
    suspend fun getFiles(owner: String, repo: String, path: String=""): Result<List<FileItem>>
}