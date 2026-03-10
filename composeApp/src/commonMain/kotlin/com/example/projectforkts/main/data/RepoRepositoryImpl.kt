package com.example.projectforkts.main.data

import com.example.projectforkts.main.domain.RepoItem
import com.example.projectforkts.main.domain.RepoRepository

class RepoRepositoryImpl(private val api: GitHubApi): RepoRepository {
    private var cache: List<RepoItem> = emptyList()

    override suspend fun searchRepos(query: String, page: Int): Result<List<RepoItem>> {
        return try {
            val response = api.searchRepos(query, page)
            val items = response.items.map { it.toDomain() }
            cache = items
            Result.success(items)
        } catch (e: Exception) {
            if (cache.isNotEmpty()) {
                Result.success(cache)
            } else {
                Result.failure(e)
            }
        }
    }

    private fun RepoResponse.toDomain() = RepoItem(
        id = id,
        name = name,
        description = description ?: "Нет описания",
        language = language,
        stars = stars,
        owner = owner.login,
        avatarUrl = owner.avatarUrl
    )
}