package com.example.projectforkts.presentation.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainUiState(
    val items: List<RepoItem> = emptyList()
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
    private val _state = MutableStateFlow(
        MainUiState(
            items = List(20) { index ->
                RepoItem(
                    id = index.toLong(),
                    name = "Репозиторий $index",
                    description = "Описание для репозитория $index",
                    stars = (index * 10) % 500,
                    owner = "Пользователь $index"
                )
            }
        )
    )
    val state: StateFlow<MainUiState> = _state.asStateFlow()
}