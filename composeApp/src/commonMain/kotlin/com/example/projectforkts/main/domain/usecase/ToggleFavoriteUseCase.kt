package com.example.projectforkts.main.domain.usecase

import com.example.projectforkts.main.domain.model.RepoItem
import com.example.projectforkts.main.domain.repository.FavoriteRepository

class ToggleFavoriteUseCase(private val repository: FavoriteRepository) {
    suspend operator fun invoke(repo: RepoItem, isFavorite: Boolean): Result<Unit> {
        return if (isFavorite) {
            repository.removeFavorite(repo)
        } else {
            repository.addFavorite(repo)
        }
    }
}