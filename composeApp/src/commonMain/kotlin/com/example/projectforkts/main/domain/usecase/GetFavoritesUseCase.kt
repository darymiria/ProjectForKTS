package com.example.projectforkts.main.domain.usecase

import com.example.projectforkts.main.domain.model.RepoItem
import com.example.projectforkts.main.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(private val repository: FavoriteRepository) {
    operator fun invoke(): Flow<List<RepoItem>> = repository.getAllFavorites()
}