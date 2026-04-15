package com.example.projectforkts.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.projectforkts.main.data.RepoRepositoryImpl
import com.example.projectforkts.main.domain.repository.RepoRepository
import com.example.projectforkts.main.domain.UnauthorizedException
import com.example.projectforkts.main.domain.model.RepoItem
import com.example.projectforkts.main.domain.usecase.GetReposUseCase
import kotlinx.coroutines.CancellationException
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


class MainViewModel(private val getReposUseCase: GetReposUseCase) : ViewModel() {

    private val _unauthorizedEvent = Channel<Unit>(Channel.BUFFERED)
    val unauthorizedEvent = _unauthorizedEvent.receiveAsFlow()
    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state.asStateFlow()
    private val _query = MutableStateFlow("kotlin")
    private var searchJob: Job? = null

    init{
        observeQuery()
    }

    private fun observeQuery() {
        viewModelScope.launch {
            _query
                .debounce(500L)
                .collect { query ->
                    if (query.isBlank()) {
                        _state.update { it.copy(items = emptyList(), error = null) }
                    } else {
                        search(query)
                    }
                }
        }
    }
    fun onQueryChanged(query: String) {
        _state.update { it.copy(query = query) }
        _query.value = query
    }

    private fun search(query: String, page: Int=1){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, currentPage = page) }
            getReposUseCase(query, page)
                .onSuccess { items ->  handleSearchSuccess(items, query, page) }
                .onFailure { e -> handleFailure(e) }
        }

    }
    private fun handleSearchSuccess(items: List<RepoItem>, query: String, page: Int ){
        _state.update {
            it.copy(
                items = if (page==1) items else it.items+items,
                isLoading = false,
                currentPage = page,
                hasNextPage = items.size == 20,
                isFromCache = false,
                query = query,
                error = null,
            )
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
                    isRefreshing = false,
                    error = e.message,
                    isFromCache = it.items.isNotEmpty()
                )
            }
    }


    fun loadNextPage() {
        val current = _state.value
        if (current.isLoading || !current.hasNextPage) return
        search(current.query, current.currentPage + 1)
    }

        fun retry() {
            search(_state.value.query)
        }

        fun refresh() {
            _state.update {it.copy(isRefreshing = true)}
            search(_state.value.query, page = 1)
            }

            override fun onCleared() {
                super.onCleared()
                searchJob?.cancel()
            }
        }
