package com.example.projectforkts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.projectforkts.login.presentation.LoginScreen
import com.example.projectforkts.login.presentation.LoginViewModel
import com.example.projectforkts.navigation.MainView
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.example.projectforkts.core.AppStorage
import com.example.projectforkts.core.TokenStorage
import com.example.projectforkts.main.data.db.appContext
import com.example.projectforkts.navigation.LoginScreen
import com.example.projectforkts.navigation.MainScreen
import com.example.projectforkts.navigation.WelcomeScreen
import io.ktor.http.parameters
import io.ktor.http.parametersOf
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    private val appStorage: AppStorage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        Napier.base(DebugAntilog())
        appContext = applicationContext
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val isOnboardingCompleted by appStorage.isOnboardingCompleted
                .collectAsState(initial = false)
            val savedToken by appStorage.accessToken
                .collectAsState(initial = null)
            savedToken?.let { TokenStorage.accessToken = it }
            val  startDestination = when {
                !isOnboardingCompleted -> WelcomeScreen
                savedToken != null -> MainScreen
                else -> LoginScreen
            }
            val loginViewModel = koinViewModel<com.example.projectforkts.login.presentation.LoginViewModel>()
            MainView(startDestination = startDestination, onOnboardingComplete = {
                    lifecycleScope.launch {
                        appStorage.setOnboardingCompleted()
                    }
                },
                appStorage = appStorage,
                loginScreen = { onLoginSuccess ->
                LoginScreen(onLoginSuccess = onLoginSuccess,
                    viewModel = loginViewModel
                )
            }
            )
        }
    }
}
