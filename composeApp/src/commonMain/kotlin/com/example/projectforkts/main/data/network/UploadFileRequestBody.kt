package com.example.projectforkts.main.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadFileRequestBody(
    @SerialName("message") val message: String,
    @SerialName("content") val content: String,
    @SerialName("sha") val sha: String? = null
)
