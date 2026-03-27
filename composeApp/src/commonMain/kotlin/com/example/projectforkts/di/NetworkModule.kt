package com.example.projectforkts.di

import com.example.projectforkts.main.data.network.GitHubApi
import org.koin.dsl.module

val networkModule = module{
    single { GitHubApi() }
}