package com.example.projectforkts.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( "repos")
data class RepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String,
    val language: String?,
    val stars: Int,
    val owner: String,
    val avatarUrl: String?,
    val query: String
)

