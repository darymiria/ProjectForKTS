package com.example.projectforkts.main.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projectforkts.main.data.db.RepoDao
import com.example.projectforkts.main.data.db.RepoEntity

@Database(entities = [RepoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}