package com.example.projectforkts.main.data.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RepoResponse(
    val id: Long,
    val name: String,
    val description: String? = null,
    val language: String? = null,
    @SerialName("stargazers_count") val stars: Int,
    val owner: OwnerResponse,
    )
@Serializable
data class OwnerResponse(
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String
)
@Serializable
data class SearchResponse(
    val items: List<RepoResponse>
)
