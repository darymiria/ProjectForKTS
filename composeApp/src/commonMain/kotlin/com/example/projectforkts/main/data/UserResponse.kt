package com.example.projectforkts.main.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val login: String,
    val name: String? = null,
    val bio: String? = null,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("public_repos") val publicRepos: Int,
    val followers: Int,
    val following: Int
)
