package com.example.projectforkts.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectforkts.main.domain.model.RepoItem
import com.example.projectforkts.main.domain.usecase.GetFavoritesUseCase
import com.example.projectforkts.main.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase): ViewModel() {

    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state.asStateFlow()

    private val _navigateToDetail = Channel<Pair<String, String>>(Channel.BUFFERED)
    val navigateToDetail = _navigateToDetail.receiveAsFlow()

    init {
        observeFavorites()
    }

    fun observeFavorites(){
        viewModelScope.launch {
            getFavoritesUseCase()
                .collect { items ->
                    _state.update { it.copy(items = items) }
                }
        }
    }

    fun removeFromFavorite(repo : RepoItem){
        viewModelScope.launch {
            toggleFavoriteUseCase(repo, isFavorite = true)
        }
    }

    fun onRepoClick(owner: String, repo: String) {
        viewModelScope.launch {
            _navigateToDetail.send(Pair(owner,repo))
        }
    }
}