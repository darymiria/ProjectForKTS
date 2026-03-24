package com.example.projectforkts.di

import com.example.projectforkts.core.AppStorage
import com.example.projectforkts.dataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module{
    single { AppStorage(androidContext().dataStore) }
}