package com.example.projectforkts.di

import com.example.projectforkts.main.data.FavoriteRepositoryImpl
import com.example.projectforkts.main.data.FileUploadRepositoryImpl
import com.example.projectforkts.main.data.IssueRepositoryImpl
import com.example.projectforkts.main.data.RepoDetailsRepositoryImpl
import com.example.projectforkts.main.data.RepoRepositoryImpl
import com.example.projectforkts.main.domain.repository.FavoriteRepository
import com.example.projectforkts.main.domain.repository.FileUploadRepository
import com.example.projectforkts.main.domain.repository.IssueRepository
import com.example.projectforkts.main.domain.repository.RepoDetailsRepository
import com.example.projectforkts.main.domain.repository.RepoRepository
import com.example.projectforkts.profile.domain.ProfileRepository
import com.example.projectforkts.profile.domain.ProfileRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module{
    single<RepoRepository> { RepoRepositoryImpl(get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    single<RepoDetailsRepository> { RepoDetailsRepositoryImpl(get()) }
    single<IssueRepository>{ IssueRepositoryImpl(get()) }
    single<FileUploadRepository>{ FileUploadRepositoryImpl(get()) }
    single<FavoriteRepository>{ FavoriteRepositoryImpl(get(), get()) }
}