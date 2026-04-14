package com.example.projectforkts.di

import com.example.projectforkts.auth.AppAuth
import com.example.projectforkts.auth.AuthClient
import net.openid.appauth.AuthorizationService
import org.koin.dsl.module
import android.app.Application
import com.example.projectforkts.login.presentation.LoginViewModel
import io.ktor.http.parameters
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf

val authModule = module {
    single { AppAuth()}
    single { AuthClient(get(), get()) }
    single { AuthorizationService(androidContext()) }
    viewModel {
        LoginViewModel(
            application = androidApplication(),
            authClient = get(),
            authService = get()
        )
    }
}