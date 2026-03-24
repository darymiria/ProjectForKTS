package com.example.projectforkts.login.presentation

import android.app.Application
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projectforkts.core.AppStorage
import com.example.projectforkts.auth.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer


class LoginViewModel(application: Application, private val appStorage: AppStorage) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(appStorage)
    private val authService = AuthorizationService(getApplication())

    private val openAuthPageChannel = Channel<Intent>(Channel.BUFFERED)
    private val authSuccessChannel = Channel<Unit>(Channel.BUFFERED)
    private val errorChannel = Channel<String>(Channel.BUFFERED)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading.asStateFlow()

    val openAuthPageFlow: Flow<Intent> = openAuthPageChannel.receiveAsFlow()
    val authSuccessFlow: Flow<Unit> = authSuccessChannel.receiveAsFlow()
    val errorFlow: Flow<String> = errorChannel.receiveAsFlow()

    fun openLoginPage() {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val authRequest = authRepository.getAuthRequest()
        val intent = authService.getAuthorizationRequestIntent(authRequest, customTabsIntent)
        openAuthPageChannel.trySendBlocking(intent)
    }

    fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                authRepository.performTokenRequest(authService, tokenRequest)
            }.onSuccess {
                _isLoading.value = false
                authSuccessChannel.send(Unit)
            }.onFailure {
                _isLoading.value = false
                errorChannel.send(it.message ?: "Ошибка авторизации")
            }
        }
    }
    fun onAuthCodeFailed(exception: AuthorizationException) {
        errorChannel.trySendBlocking(exception.message ?: "Авторизация отменена")
    }
    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }

    companion object {
        fun factory(appStorage: AppStorage) = viewModelFactory {
            initializer {
                LoginViewModel(
                    application = this[APPLICATION_KEY]!!,
                    appStorage = appStorage
                )
            }
        }
    }
}


