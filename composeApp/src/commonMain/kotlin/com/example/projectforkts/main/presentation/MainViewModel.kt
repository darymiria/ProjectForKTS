package com.example.projectforkts.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectforkts.main.data.RepoRepositoryImpl
import com.example.projectforkts.main.domain.RepoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel(private val repoRepository: RepoRepository = RepoRepositoryImpl()) : ViewModel() {

    private val _unauthorizedEvent = Channel<Unit>(Channel.BUFFERED)
    val unauthorizedEvent = _unauthorizedEvent.receiveAsFlow()
    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state.asStateFlow()
    private val _query = MutableStateFlow("kotlin")

    fun onQueryChanged(query: String){
        _state.update{it.copy(query = query)}
        _query.value = query
    }

    init{
        viewModelScope.launch {
            _query
                .debounce(500L)
                .flatMapLatest { query ->
                    flow{
                        _state.update { it.copy(isLoading = true, error = null, currentPage = 1) }
                        repoRepository.searchRepos(query, page = 1)
                            .onSuccess { items ->
                                _state.update {
                                    it.copy(
                                        items = items,
                                        isLoading = false,
                                        currentPage = 1,
                                        hasNextPage = items.size == 20,
                                        isFromCache = false,
                                        query = query,
                                    )
                                }
                            }
                            .onFailure { exception ->
                                if (exception is RepoRepositoryImpl.UnauthorizedException) {
                                    _unauthorizedEvent.send(Unit)
                                } else {
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            error = exception.message,
                                            isFromCache = it.items.isNotEmpty()
                                        )
                                    }
                                }
                            }
                        emit(Unit)
                    }
                }
                .collect()
        }
    }
    private var searchJob: Job? = null


    fun loadNextPage() {
        val current = _state.value
        if (!current.isLoading && current.hasNextPage) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                repoRepository.searchRepos(current.query, current.currentPage + 1)
                    .onSuccess { items ->
                        _state.update {
                            it.copy(
                                items = items,
                                isLoading = false,
                                currentPage = 1,
                                hasNextPage = items.size == 20,
                            )
                        }
                    }
                    .onFailure { exception ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message,
                            )
                        }
                    }
            }
        }
    }

    fun retry() {
        _query.value = _state.value.query
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}