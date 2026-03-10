package com.example.projectforkts.main.domain

import com.example.projectforkts.main.domain.RepoItem
interface RepoRepository {
    suspend fun searchRepos(query: String, page: Int): Result<List<RepoItem>>
}