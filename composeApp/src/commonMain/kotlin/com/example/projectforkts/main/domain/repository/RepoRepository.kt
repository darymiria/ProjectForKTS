package com.example.projectforkts.main.domain.repository

import com.example.projectforkts.main.domain.model.RepoItem

interface RepoRepository {
    suspend fun searchRepos(query: String, page: Int): Result<List<RepoItem>>
}