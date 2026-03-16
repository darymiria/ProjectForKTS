package com.example.projectforkts.main.data

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.Logger
import com.example.projectforkts.main.data.TokenStorage
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer


object GitHubApi{
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(message, tag = "GitHubApi")
                }
            }
            level = LogLevel.BODY
        }
        install(Auth)
        {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = TokenStorage.accessToken ?: "",
                        refreshToken = ""
                    )
                }
                sendWithoutRequest { true }
            }
        }
    }
    suspend fun searchRepos( query: String, page: Int, perPage: Int = 20): SearchResponse {
        return client.get("https://api.github.com/search/repositories") {
            parameter("q", query)
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
    }

}

