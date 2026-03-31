package com.example.projectforkts.main.data

import com.example.projectforkts.main.data.network.FileItemResponse
import com.example.projectforkts.main.data.network.GitHubApi
import com.example.projectforkts.main.data.network.RepoDetailsResponse
import com.example.projectforkts.main.domain.model.FileItem
import com.example.projectforkts.main.domain.repository.RepoDetailsRepository
import com.example.projectforkts.main.domain.model.RepoDetails
import io.ktor.utils.io.CancellationException
import kotlin.io.encoding.Base64

class RepoDetailsRepositoryImpl (private val api: GitHubApi): RepoDetailsRepository {

    override suspend fun getRepoDetails(owner: String, repo: String): Result<RepoDetails> =
        try {
            Result.success(api.getReposDetails(owner, repo).toDomain())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }


    override suspend fun getFiles(
        owner: String,
        repo: String,
        path: String
    ): Result<List<FileItem>> =
        try {
            val files = api.getFiles(owner, repo, path).map { it.toDomain() }
            Result.success(files)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getReadme(owner: String, repo: String): Result<String> =
        try {
            val response = api.getReadme(owner, repo)
            val decoded = String(
                java.util.Base64.getDecoder().decode(response.content.replace("\n", "")),
                Charsets.UTF_8
            )
            Result.success(decoded)
        } catch (e: CancellationException) {
                throw e
        } catch(e: Exception){
            Result.failure(e)
        }


    private fun RepoDetailsResponse.toDomain() = RepoDetails(
        id = id,
        name = name,
        fullName = fullName,
        description = description ?: "Нет описания",
        language = language,
        stars = stars,
        forks = forks,
        openIssues = openIssues,
        owner = owner.login,
        avatarUrl = owner.avatarUrl,
        htmlUrl = htmlUrl
    )

    private fun FileItemResponse.toDomain() = FileItem(
        name = name,
        path = path,
        type = type,
        downloadUrl = downloadUrl
    )
}
