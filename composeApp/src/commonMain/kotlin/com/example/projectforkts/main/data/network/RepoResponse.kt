package com.example.projectforkts.main.data.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RepoResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("language") val language: String? = null,
    @SerialName("stargazers_count") val stars: Int,
    @SerialName("owner") val owner: OwnerResponse,
    )
@Serializable
data class OwnerResponse(
    @SerialName("login") val login: String,
    @SerialName("avatar_url") val avatarUrl: String
)
@Serializable
data class SearchResponse(
    @SerialName("items") val items: List<RepoResponse>
)
