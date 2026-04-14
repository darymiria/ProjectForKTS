package com.example.projectforkts.auth

import com.example.projectforkts.core.AppStorage
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthClient(private val appStorage: AppStorage, private val appAuth: AppAuth) {
    fun getAuthRequest() = appAuth.getAuthRequest()

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest
    ) {
        val tokens = appAuth.performTokenRequestSuspend(authService, tokenRequest)
        appStorage.saveToken(tokens.accessToken)
    }

    suspend fun logout() {
        appStorage.clearToken()
    }
}