package com.example.projectforkts.di

import com.example.projectforkts.main.domain.usecase.GetReposUseCase
import com.example.projectforkts.profile.domain.GetProfileUseCase
import com.example.projectforkts.profile.domain.LogoutUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetReposUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { LogoutUseCase(get()) }
}