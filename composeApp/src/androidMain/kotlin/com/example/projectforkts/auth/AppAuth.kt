package com.example.projectforkts.auth

import android.net.Uri
import androidx.core.net.toUri
import net.openid.appauth.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AppAuth {
    private val serviceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTH_URI),
        Uri.parse(AuthConfig.TOKEN_URI)
    )

    fun getAuthRequest(): AuthorizationRequest {
        return AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID,
            ResponseTypeValues.CODE,
            AuthConfig.CALLBACK_URL.toUri()
        )
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest
    ): TokensModel = suspendCoroutine { continuation ->
            authService.performTokenRequest(
                tokenRequest,
                getClientAuthentication()
            ) { response, ex ->
                when {
                    ex != null -> continuation.resumeWithException(ex)
                    response != null -> continuation.resume(TokensModel(accessToken = response.accessToken.orEmpty())
                    )
                    else -> error("unreachable")
                }
            }
        }

    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }

    private object AuthConfig {
        const val AUTH_URI = "https://github.com/login/oauth/authorize"
        const val TOKEN_URI = "https://github.com/login/oauth/access_token"
        const val SCOPE = "user,repo"
        const val CLIENT_ID = "Ov23liZuys7yBQG5rXuA"
        const val CLIENT_SECRET = "60351a4816be45c24495ff5faaecb34cf86f0569"
        const val CALLBACK_URL = "com.example.projectforkts://callback"
    }
}