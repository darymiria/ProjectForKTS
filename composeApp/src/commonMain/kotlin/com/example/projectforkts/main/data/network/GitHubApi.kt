package com.example.projectforkts.main.data.network

import com.example.projectforkts.core.TokenStorage
import com.example.projectforkts.main.data.network.UserResponse
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingConfig
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.CancellationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GitHubApi{
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
        install(Auth) {
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

    suspend fun getUserProfile(): UserResponse {
        return client.get("https://api.github.com/user").body()
    }

    suspend fun getReposDetails( owner: String, repo: String): RepoDetailsResponse {
        return client.get("https://api.github.com/repos/$owner/$repo").body()
    }

    suspend fun getReadme(owner: String, repo: String): ReadmeResponse{
        return client.get("https://api.github.com/repos/$owner/$repo/readme").body()
    }

    suspend fun getFiles(owner: String, repo: String, path: String): List<FileItemResponse>{
        return client.get("https://api.github.com/repos/$owner/$repo/contents/$path").body()
    }

    suspend fun createIssue(owner: String, repo: String, request: CreateIssueRequestBody): Unit {
        client.post("https://api.github.com/repos/$owner/$repo/issues") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun getFileSha(owner: String, repo: String, path: String): String? {
        return try{
            val response: FileContentResponse = client
                .get("https://api.github.com/repos/$owner/$repo/contents/$path")
                .body()
            response.sha
        } catch(e: CancellationException){
            throw e
        }
        catch(e: Exception){
            null
        }
    }

    suspend fun uploadFile(
        owner: String,
        repo: String,
        path: String,
        body: UploadFileRequestBody
    ) {
        client.put("https://api.github.com/repos/$owner/$repo/contents/$path") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    suspend fun isStarred(owner: String, repo: String): Boolean {
        return try {
            client.get("https://api.github.com/user/starred/$owner/$repo")
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun starRepo(owner: String, repo: String) {
        client.put("https://api.github.com/user/starred/$owner/$repo"){
            header("Content-Length", "0")
        }
    }

    suspend fun unstarRepo(owner: String, repo: String) {
        client.delete("https://api.github.com/user/starred/$owner/$repo")
    }
    @Serializable
    data class FileContentResponse(val sha: String)
}
