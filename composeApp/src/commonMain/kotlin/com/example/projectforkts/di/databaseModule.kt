package com.example.projectforkts.di

import com.example.projectforkts.main.data.db.createDatabase
import org.koin.dsl.module

val databaseModule = module{
    single { createDatabase() }
    single {get<com.example.projectforkts.main.data.db.AppDatabase>().repoDao()}
}