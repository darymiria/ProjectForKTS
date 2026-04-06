package com.example.projectforkts.di

import com.example.projectforkts.main.domain.usecase.CreateIssueUseCase
import com.example.projectforkts.main.domain.usecase.GetFilesUseCase
import com.example.projectforkts.main.domain.usecase.GetReadmeUseCase
import com.example.projectforkts.main.domain.usecase.GetRepoDetailsUseCase
import com.example.projectforkts.main.domain.usecase.GetReposUseCase
import com.example.projectforkts.profile.domain.GetProfileUseCase
import com.example.projectforkts.profile.domain.LogoutUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetReposUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory{ GetRepoDetailsUseCase(get()) }
    factory{GetFilesUseCase(get())}
    factory{ GetReadmeUseCase(get()) }
    factory { CreateIssueUseCase(get()) }
}