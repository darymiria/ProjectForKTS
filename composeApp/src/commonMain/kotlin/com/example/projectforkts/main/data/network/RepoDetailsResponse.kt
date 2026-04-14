package com.example.projectforkts.main.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailsResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("description") val description: String? = null,
    @SerialName("language") val language: String? = null,
    @SerialName("stargazers_count") val stars: Int,
    @SerialName("forks_count") val forks: Int,
    @SerialName("open_issues_count") val openIssues: Int,
    @SerialName("owner") val owner: OwnerResponse,
    @SerialName("html_url") val htmlUrl: String
)

@Serializable
data class ReadmeResponse(
    @SerialName("content") val content: String,
    @SerialName("encoding") val encoding: String,
)

@Serializable
data class FileItemResponse(
    @SerialName("name") val name: String,
    @SerialName("path") val path: String,
    @SerialName("type") val type: String,
    @SerialName("download_url") val downloadUrl: String? = null
)

