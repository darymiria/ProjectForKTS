package com.example.projectforkts.main.domain

data class RepoItem(
    val id: Long,
    val name: String,
    val description: String,
    val language: String?,
    val stars: Int,
    val owner: String,
    val avatarUrl: String? = null
)
