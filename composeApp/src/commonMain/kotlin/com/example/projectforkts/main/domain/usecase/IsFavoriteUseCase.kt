package com.example.projectforkts.main.domain.usecase

import com.example.projectforkts.main.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class IsFavoriteUseCase(private val repository: FavoriteRepository) {
    operator fun invoke(id: Long): Flow<Boolean> = repository.isFavorite(id)
}