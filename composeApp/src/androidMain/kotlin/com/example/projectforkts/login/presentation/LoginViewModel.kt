package com.example.projectforkts.login.presentation

import android.app.Application
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projectforkts.core.AppStorage
import com.example.projectforkts.auth.AuthClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import kotlinx.coroutines.CancellationException


class LoginViewModel(
    application: Application,
    private val authClient: AuthClient,
    private val authService: AuthorizationService)
    : AndroidViewModel(application) {


    private val openAuthPageChannel = Channel<Intent>(Channel.BUFFERED)
    private val authSuccessChannel = Channel<Unit>(Channel.BUFFERED)
    private val errorChannel = Channel<String>(Channel.BUFFERED)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading.asStateFlow()

    val openAuthPageFlow: Flow<Intent> = openAuthPageChannel.receiveAsFlow()
    val authSuccessFlow: Flow<Unit> = authSuccessChannel.receiveAsFlow()
    val errorFlow: Flow<String> = errorChannel.receiveAsFlow()

    fun createAuthIntent(): Intent {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val authRequest = authClient.getAuthRequest()
        return authService.getAuthorizationRequestIntent(authRequest, customTabsIntent)
    }

    fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                authClient.performTokenRequest(authService, tokenRequest)
                _isLoading.value = false
                authSuccessChannel.send(Unit)
            } catch (e: CancellationException) {
                _isLoading.value = false
                throw e
            } catch(e: Exception) {
                _isLoading.value = false
                errorChannel.send(e.message ?: "Ошибка авторизации")
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

}


