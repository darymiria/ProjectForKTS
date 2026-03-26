package com.example.projectforkts.main.data

import com.example.projectforkts.main.domain.RepoItem
import com.example.projectforkts.main.domain.RepoRepository

class RepoRepositoryImpl(private val database: AppDatabase = createDatabase()): RepoRepository {
    private val dao = database.repoDao()

    override suspend fun searchRepos(query: String, page: Int): Result<List<RepoItem>> {
        return try {
            val response = GitHubApi.searchRepos(query, page)
            val items = response.items.map { it.toDomain() }
            if (page == 1) {
                dao.deleteByQuery(query)
                dao.insertAll(items.map { it.toEntity(query) })
            }
            Result.success(items)
        }
        catch (e: Exception) {
            val cached = dao.getReposByQuery(query)
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }
    private fun RepoEntity.toDomain() = RepoItem(
        id = id,
        name = name,
        description = description,
        language = language,
        stars = stars,
        owner = owner,
        avatarUrl = avatarUrl
    )
    private fun RepoResponse.toDomain() = RepoItem(
        id = id,
        name = name,
        description = description ?: "Нет описания",
        language = language,
        stars = stars,
        owner = owner.login,
        avatarUrl = owner.avatarUrl
    )
    private fun RepoItem.toEntity(query: String) = RepoEntity(
        id = id,
        name = name,
        description = description,
        language = language,
        stars = stars,
        owner = owner,
        avatarUrl = avatarUrl,
        query = query
    )
}
