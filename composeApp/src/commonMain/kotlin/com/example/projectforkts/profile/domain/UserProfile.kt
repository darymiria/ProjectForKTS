package com.example.projectforkts.profile.domain

data class UserProfile(
    val login: String,
    val name: String?,
    val bio: String?,
    val avatarUrl: String,
    val publicRepos: Int,
    val followers: Int,
    val following: Int
)
