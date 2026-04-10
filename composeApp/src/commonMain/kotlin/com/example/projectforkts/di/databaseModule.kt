package com.example.projectforkts.di

import com.example.projectforkts.main.data.db.createDatabase
import org.koin.dsl.module
import com.example.projectforkts.main.data.db.AppDatabase

val databaseModule = module{
    single { createDatabase() }
    single {get <AppDatabase>().repoDao()}
    single{get<AppDatabase>().favoriteDao()}
}