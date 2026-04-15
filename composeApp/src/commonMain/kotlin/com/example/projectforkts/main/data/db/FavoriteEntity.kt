package com.example.projectforkts.main.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String,
    val language: String?,
    val stars: Int,
    val owner: String,
    val avatarUrl: String?
)
