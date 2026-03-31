package com.example.projectforkts.main.domain.model

data class RepoDetails(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val openIssues: Int,
    val owner: String,
    val avatarUrl: String,
    val htmlUrl: String
    )
