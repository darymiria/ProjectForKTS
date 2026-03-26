package com.example.projectforkts.main.data

import android.content.Context
import androidx.room.Room

lateinit var appContext: Context

actual fun createDatabase(): AppDatabase {
    return Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "app_database"
    ).build()
}
