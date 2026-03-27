package com.example.projectforkts.main.domain.usecase

import com.example.projectforkts.main.domain.model.RepoItem
import com.example.projectforkts.main.domain.repository.RepoRepository

class GetReposUseCase(private val repository: RepoRepository) {
    suspend operator fun invoke(query: String, page: Int): Result<List<RepoItem>> = repository.searchRepos(query, page)
}