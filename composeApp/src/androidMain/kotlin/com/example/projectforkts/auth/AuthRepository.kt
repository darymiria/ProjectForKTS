package com.example.projectforkts.auth

import com.example.projectforkts.main.data.TokenStorage
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthRepository {
    fun getAuthRequest() = AppAuth.getAuthRequest()

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest
    ) {
        val tokens = AppAuth.performTokenRequestSuspend(authService, tokenRequest)
        TokenStorage.accessToken = tokens.accessToken
    }

    fun logout() {
        TokenStorage.accessToken = null
    }
}