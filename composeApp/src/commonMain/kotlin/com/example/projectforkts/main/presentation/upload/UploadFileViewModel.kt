package com.example.projectforkts.main.presentation.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectforkts.main.domain.model.UploadFileRequest
import com.example.projectforkts.main.domain.usecase.UploadFileUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UploadFileViewModel(private val uploadFileUseCase: UploadFileUseCase): ViewModel() {

    private val _state = MutableStateFlow(UploadFileUiState())
    val state: StateFlow<UploadFileUiState> = _state.asStateFlow()

    private val _successEvent = Channel<Unit>(Channel.BUFFERED)
    val successEvent = _successEvent.receiveAsFlow()

    fun onFilePathChanged(path: String){
        _state.update{ it.copy(filePath = path, error = null)}
    }
    fun onCommitMessageChanged(message: String){
        _state.update{ it.copy(commitMessage = message, error = null)}
    }
    fun onFileSelected(name: String, bytes: ByteArray){
        _state.update{ it.copy(fileName = name, fileBytes = bytes, error = null)}
    }
    fun upload(owner: String, repo: String){
        val current = _state.value
        when{
            current.fileBytes == null -> {
                _state.update { it.copy(error ="Выберите файл") }
                return
            }
            current.filePath.isBlank() -> {
                 _state.update{ it.copy(error = "Укажите путь")}
                return
            }
            current.commitMessage.isBlank() -> {
                _state.update{ it.copy(error = "Введите описание коммита")}
                return
            }
        }

        viewModelScope.launch{
            _state.update{it.copy(isLoading = true, error = null)}
            uploadFileUseCase(
                owner, repo, UploadFileRequest(
                    path = current.filePath,
                    content = current.fileBytes!!,
                    commitMessage = current.commitMessage
                )
            )
                .onSuccess {
                    _state.update{it.copy(isLoading = false)}
                    _successEvent.send(Unit)
                }
                .onFailure { e ->
                    if (e is CancellationException) throw e
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}