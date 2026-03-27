package com.example.projectforkts.di

import com.example.projectforkts.main.presentation.MainViewModel
import com.example.projectforkts.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
}