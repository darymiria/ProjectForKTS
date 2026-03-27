package com.example.projectforkts.di

import com.example.projectforkts.auth.AppAuth
import com.example.projectforkts.auth.AuthClient
import net.openid.appauth.AuthorizationService
import org.koin.dsl.module
import android.app.Application
import com.example.projectforkts.login.presentation.LoginViewModel
import io.ktor.http.parameters
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf

val authModule = module {
    single { AuthClient(get(), get()) }
    single { (app: Application) ->
        AuthorizationService(app)
    }
    viewModel {(application : Application) ->
        LoginViewModel(
            application = application,
            authClient = get(),
            authService = get(parameters = { parametersOf(application) })
        )
    }
}