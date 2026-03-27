package com.example.projectforkts.di

import com.example.projectforkts.main.data.RepoRepositoryImpl
import com.example.projectforkts.main.domain.repository.RepoRepository
import com.example.projectforkts.profile.domain.ProfileRepository
import com.example.projectforkts.profile.domain.ProfileRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module{
    single<RepoRepository> { RepoRepositoryImpl(get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
}