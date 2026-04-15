package com.example.projectforkts.main.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projectforkts.main.data.db.RepoDao
import com.example.projectforkts.main.data.db.RepoEntity

@Database(entities = [RepoEntity::class, FavoriteEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun favoriteDao(): FavoriteDao
}