package com.example.projectforkts.main.domain.repository

import com.example.projectforkts.main.domain.model.RepoItem
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getAllFavorites(): Flow<List<RepoItem>>
    suspend fun addFavorite(repo: RepoItem): Result<Unit>
    suspend fun removeFavorite(repo: RepoItem): Result<Unit>

    fun isFavorite(id: Long): Flow<Boolean>

    suspend fun starRepo(owner: String, repo: String): Result<Unit>
    suspend fun unstarRepo(owner: String, repo: String): Result<Unit>
    suspend fun isStarredOnGitHub(owner: String, repo: String): Result<Boolean>

}