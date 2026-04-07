package com.example.projectforkts.main.data

import com.example.projectforkts.main.data.network.GitHubApi
import com.example.projectforkts.main.data.network.UploadFileRequestBody
import com.example.projectforkts.main.domain.model.UploadFileRequest
import com.example.projectforkts.main.domain.repository.FileUploadRepository
import io.ktor.utils.io.CancellationException
import java.util.Base64

class FileUploadRepositoryImpl(private val api: GitHubApi): FileUploadRepository {
    override suspend fun uploadFile(
        owner: String,
        repo: String,
        request: UploadFileRequest
    ): Result<Unit> = try {
        val sha = api.getFileSha(owner, repo, request.path)
        val base64Content = Base64.getEncoder().encodeToString(request.content)
        api.uploadFile(
            owner, repo, request.path, UploadFileRequestBody(
                message = request.commitMessage,
                content = base64Content,
                sha = sha
            )
        )
        Result.success(Unit)
    } catch(e: CancellationException) {
        throw e
    } catch(e:Exception){
        Result.failure(e)
    }

}