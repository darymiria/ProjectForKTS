package com.example.projectforkts.di

import com.example.projectforkts.favorites.presentation.FavoritesViewModel
import com.example.projectforkts.main.presentation.MainViewModel
import com.example.projectforkts.main.presentation.detail.RepoDetailViewModel
import com.example.projectforkts.main.presentation.issue.CreateIssueViewModel
import com.example.projectforkts.main.presentation.upload.UploadFileViewModel
import com.example.projectforkts.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { RepoDetailViewModel(get(), get(),
        get(), get(), get()) }
    viewModel { CreateIssueViewModel(get()) }
    viewModel { UploadFileViewModel(get()) }
    viewModel { FavoritesViewModel(get(), get()) }
}