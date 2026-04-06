package com.example.projectforkts.main.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateIssueRequestBody(
    @SerialName("title") val title: String,
    @SerialName("body") val body: String,
)