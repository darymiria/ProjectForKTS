package com.example.projectforkts.main.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("login") val login: String,
    @SerialName("name") val name: String? = null,
    @SerialName("bio") val bio: String? = null,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("public_repos") val publicRepos: Int,
    @SerialName("followers")val followers: Int,
    @SerialName("following")val following: Int
)