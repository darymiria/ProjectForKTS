package com.example.projectforkts.auth

import com.example.projectforkts.AppStorage
import com.example.projectforkts.main.data.TokenStorage
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthRepository(private val appStorage: AppStorage) {
    fun getAuthRequest() = AppAuth.getAuthRequest()

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest
    ) {
        val tokens = AppAuth.performTokenRequestSuspend(authService, tokenRequest)
        appStorage.saveToken(tokens.accessToken)
    }

    suspend fun logout() {
        appStorage.clearToken()
    }
}