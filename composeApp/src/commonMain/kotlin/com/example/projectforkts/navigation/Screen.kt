package com.example.projectforkts.navigation

import kotlinx.serialization.Serializable

@Serializable
object WelcomeScreen
@Serializable
object LoginScreen
@Serializable
object MainScreen
@Serializable
object ReposScreen
@Serializable
object ProfileScreen
@Serializable
data class RepoDetailScreen(val owner: String, val repo: String)

@Serializable
data class CreateIssueScreen(val owner: String, val repo: String)

@Serializable
data class UploadFileScreen(val owner: String, val repo: String)

@Serializable
object FavoritesScreen