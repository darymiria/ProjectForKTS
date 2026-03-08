package com.example.projectforkts.presentation.main

import androidx.lifecycle.ViewModel
import com.example.projectforkts.repository.RepoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.internal.throwMissingFieldException

data class MainUiState(
    val items: List<RepoItem> = emptyList(),
    val error: String? = null
)

data class RepoItem(
    val id: Long,
    val name: String,
    val description: String,
    val stars: Int,
    val owner: String,
    val avatarUrl: String? = null
)

class MainViewModel : ViewModel() {

    private val repoRepository = RepoRepository()

    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state.asStateFlow()

    init {
        loadRepos()
    }

    private fun loadRepos() {
        repoRepository.getList()
            .onSuccess { items ->
                _state.update { it.copy(items = items) }
            }
            .onFailure { exception ->
                _state.update { it.copy(error = exception.message) }
            }
    }
}