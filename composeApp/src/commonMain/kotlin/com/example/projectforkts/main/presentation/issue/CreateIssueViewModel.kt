package com.example.projectforkts.main.presentation.issue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectforkts.main.domain.model.CreateIssueRequest
import com.example.projectforkts.main.domain.usecase.CreateIssueUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateIssueViewModel(private val createIssueUseCase: CreateIssueUseCase): ViewModel() {

    private val _state = MutableStateFlow(CreateIssueUiState())
    val state: StateFlow<CreateIssueUiState> = _state.asStateFlow()

    private val _successEvent = Channel<Unit>(Channel.BUFFERED)
    val successEvent = _successEvent.receiveAsFlow()

    fun onTitleChanged(title: String){
        _state.update {it.copy(title = title, error = null)}
    }

    fun onBodyChanged(body: String){
        _state.update { it.copy(body = body, error = null) }
    }

    fun createIssue(owner: String, repo: String){
        val current = _state.value
        if (current.title.isBlank()){
            _state.update { it.copy(error = "Заголовок не может быть пустым") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            createIssueUseCase(owner, repo, CreateIssueRequest(current.title, current.body))
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _successEvent.send(Unit)
                }
                .onFailure { e ->
                    if (e is CancellationException) throw e
                    _state.update {it.copy(isLoading = false, error = e.message)}
                }
        }

    }

}
