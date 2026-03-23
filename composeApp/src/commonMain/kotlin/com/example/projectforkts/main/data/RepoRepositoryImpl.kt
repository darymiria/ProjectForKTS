package com.example.projectforkts.main.data

import com.example.projectforkts.main.data.TokenStorage
import com.example.projectforkts.main.domain.RepoItem
import com.example.projectforkts.main.domain.RepoRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode

class RepoRepositoryImpl: RepoRepository {
    private var cache: List<RepoItem> = emptyList()

    override suspend fun searchRepos(query: String, page: Int): Result<List<RepoItem>> {
        return try {
            val response = GitHubApi.searchRepos(query, page)
            val items = response.items.map { it.toDomain() }
            cache = items
            Result.success(items)
        } catch(e: ClientRequestException){
            if (e.response.status == HttpStatusCode.Unauthorized) {
                TokenStorage.accessToken = null // очищаем токен
                Result.failure(UnauthorizedException())
            } else {
                Result.failure(e)
            }
        }
        catch (e: Exception) {
            if (cache.isNotEmpty()) {
                Result.success(cache)
            } else {
                Result.failure(e)
            }
        }
    }
    class UnauthorizedException : Exception("Требуется авторизация")
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