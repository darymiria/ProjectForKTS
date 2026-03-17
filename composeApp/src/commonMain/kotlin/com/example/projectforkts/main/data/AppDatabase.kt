package com.example.projectforkts.main.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RepoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}