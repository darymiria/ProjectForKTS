package com.example.projectforkts.di

import com.example.projectforkts.core.AppStorage
import org.koin.dsl.module

val storageModule = module{
    single { AppStorage(get()) }
}