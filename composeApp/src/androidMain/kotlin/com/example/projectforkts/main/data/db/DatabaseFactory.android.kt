package com.example.projectforkts.main.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

lateinit var appContext: Context

actual fun createDatabase(): AppDatabase {
    return Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "app_database"
    )
    .addMigrations(MIGRATION_1_2)
    .build()
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL( """
            CREATE TABLE IF NOT EXISTS 'Favorites'(
                id INTEGER NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                language TEXT,
                stars INTEGER NOT NULL,
                owner TEXT NOT NULL,
                avatarUrl TEXT,
                PRIMARY KEY(id)
            )
       """ )
    }
}