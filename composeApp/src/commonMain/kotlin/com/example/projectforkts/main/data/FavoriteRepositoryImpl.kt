package com.example.projectforkts.main.data

import com.example.projectforkts.main.data.db.FavoriteDao
import com.example.projectforkts.main.data.db.FavoriteEntity
import com.example.projectforkts.main.data.network.GitHubApi
import com.example.projectforkts.main.domain.model.RepoItem
import com.example.projectforkts.main.domain.repository.FavoriteRepository
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val dao: FavoriteDao,
    private val api: GitHubApi
): FavoriteRepository {
    override fun getAllFavorites(): Flow<List<RepoItem>> =
        dao.getAllFavorites().map {list -> list.map {it.toDomain()}}

    override suspend fun addFavorite(repo: RepoItem): Result<Unit> = try {
        dao.addFavorite(repo.toEntity())
        api.starRepo(repo.owner, repo.name)
        Result.success(Unit)
    } catch(e: CancellationException) {
        throw e
    } catch(e: Exception) {
        Result.success(Unit)
    }

    override suspend fun removeFavorite(repo: RepoItem): Result<Unit> = try {
        dao.removeFavorite(repo.id)
        api.unstarRepo(repo.owner, repo.name)
        Result.success(Unit)
    } catch(e: CancellationException) {
        throw e
    } catch(e: Exception) {
        Result.success(Unit)
    }

    override suspend fun unstarRepo(owner: String, repo: String): Result<Unit> = try {
        api.unstarRepo(owner, repo)
        Result.success(Unit)
    } catch(e: CancellationException) {
        throw e
    } catch(e: Exception) {
        Result.failure(e)
    }

    override suspend fun starRepo(owner: String, repo: String): Result<Unit> = try {
        api.starRepo(owner, repo)
        Result.success(Unit)
    } catch(e: CancellationException) {
        throw e
    } catch(e: Exception) {
        Result.failure(e)
    }

    override suspend fun isStarredOnGitHub(owner: String, repo: String): Result<Boolean> = try {
        val isStarred = api.isStarred(owner, repo)
        Result.success(isStarred)
    } catch(e: CancellationException) {
        throw e
    } catch(e: Exception) {
        Result.failure(e)
    }

    override fun isFavorite(id: Long): Flow<Boolean> = dao.isFavorite(id)

    private fun FavoriteEntity.toDomain() = RepoItem(
        id = id,
        name = name,
        description = description,
        language = language,
        stars = stars,
        owner = owner,
        avatarUrl = avatarUrl
    )

    private fun RepoItem.toEntity() = FavoriteEntity(
        id = id,
        name = name,
        description = description,
        language = language,
        stars = stars,
        owner = owner,
        avatarUrl = avatarUrl
    )

}