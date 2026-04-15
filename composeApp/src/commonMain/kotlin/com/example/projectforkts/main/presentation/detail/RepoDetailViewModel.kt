package com.example.projectforkts.main.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectforkts.main.domain.UnauthorizedException
import com.example.projectforkts.main.domain.model.RepoItem
import com.example.projectforkts.main.domain.usecase.GetFilesUseCase
import com.example.projectforkts.main.domain.usecase.GetReadmeUseCase
import com.example.projectforkts.main.domain.usecase.GetRepoDetailsUseCase
import com.example.projectforkts.main.domain.usecase.IsFavoriteUseCase
import com.example.projectforkts.main.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepoDetailViewModel(
    private val getRepoDetailsUseCase: GetRepoDetailsUseCase,
    private val getReadmeUseCase: GetReadmeUseCase,
    private val getFilesUseCase: GetFilesUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    ) : ViewModel() {

    private val _state = MutableStateFlow(RepoDetailUiState())
    val state: StateFlow<RepoDetailUiState> = _state.asStateFlow()

    private val _unauthorizedEvent = Channel<Unit>(Channel.BUFFERED)
    val unauthorizedEvent = _unauthorizedEvent.receiveAsFlow()

    fun loadAll(owner: String, repo: String) {
        loadDetails(owner, repo)
        loadReadme(owner, repo)
        loadFiles(owner, repo)
        checkFavoriteStatus(owner, repo)
    }


    private fun checkFavoriteStatus(owner: String, repo: String) {
        viewModelScope.launch {
            val repoId = _state.value.details?.id ?: return@launch
            isFavoriteUseCase(repoId).collect { isFav ->
                _state.update { it.copy(isFavorite = isFav) }
            }
        }
    }

    private fun observeFavoriteStatus(
        repoId: Long
    ){
        viewModelScope.launch {
            isFavoriteUseCase(repoId).collect { isFav ->
                _state.update {it.copy(isFavorite = isFav)}
            }
        }
    }

    fun toggleFavorite(){
        val details = _state.value.details ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(
                RepoItem(
                    id = details.id,
                    name = details.name,
                    description = details.description,
                    language = details.language,
                    stars = details.stars,
                    owner = details.owner,
                    avatarUrl = details.avatarUrl
                ),
                isFavorite = _state.value.isFavorite
            )
        }
    }

    private fun loadDetails(owner: String, repo: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getRepoDetailsUseCase(owner, repo)
                .onSuccess { details ->
                    _state.update { it.copy(details = details, isLoading = false) }
                    observeFavoriteStatus(details.id)
                }
                .onFailure { e -> handleFailure(e) }
        }
    }

    private fun loadReadme(owner: String, repo: String) {
        viewModelScope.launch {
            _state.update { it.copy(isReadmeLoading = true) }
            getReadmeUseCase(owner, repo)
                .onSuccess { readme ->
                    _state.update {it.copy(readme = readme, isReadmeLoading = false)}
                }
                .onFailure { _state.update { it.copy(isReadmeLoading = false) } }
        }
    }

    private fun loadFiles(owner: String, repo: String){
        viewModelScope.launch {
            _state.update { it.copy(isFilesLoading = true)}
            getFilesUseCase(owner, repo)
                .onSuccess { files ->
                    _state.update{it.copy(files = files, isFilesLoading = false)}
                }
                .onFailure {
                    _state.update { it.copy(isFilesLoading = false) }
                }
        }
    }

    private fun handleFailure(e: Throwable){
        if (e is CancellationException) throw e
        if (e is UnauthorizedException) {
            viewModelScope.launch { _unauthorizedEvent.send(Unit) }
            return
        }
        _state.update {
            it.copy(
                isLoading = false,
                error = e.message
            )
        }
    }

}
