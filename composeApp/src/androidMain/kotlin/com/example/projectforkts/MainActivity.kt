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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var authService: AuthorizationService
    private lateinit var appStorage: AppStorage

    private val authLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = result.data ?: return@registerForActivityResult
        val authResponse = AuthorizationResponse.fromIntent(intent)
        val authException = AuthorizationException.fromIntent(intent)
        when {
            authResponse != null -> viewModel.onAuthCodeReceived(authResponse.createTokenExchangeRequest())
            authException != null -> viewModel.onAuthCodeFailed(authException)
        }
    }

    private val viewModel: LoginViewModel by viewModels {LoginViewModel.factory(appStorage) }
    override fun onCreate(savedInstanceState: Bundle?) {
        Napier.base(DebugAntilog())
        authService = AuthorizationService(this)
        appStorage = AppStorage(dataStore)
        appContext = applicationContext
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val isOnboardingCompleted by appStorage.isOnboardingCompleted
                .collectAsState(initial = false)
            val savedToken by appStorage.accessToken
                .collectAsState(initial = null)
            savedToken?.let { TokenStorage.accessToken = it }
            MainView( startDestination = when {
                !isOnboardingCompleted -> WelcomeScreen
                savedToken != null -> MainScreen
                else -> LoginScreen
            }, onOnboardingComplete = {
                    lifecycleScope.launch {
                        appStorage.setOnboardingCompleted()
                    }
                },
                appStorage = appStorage,
                loginScreen = { onLoginSuccess ->
                LoginScreen(onLoginSuccess = onLoginSuccess,
                    viewModel = viewModel
                )
            }
            )
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        authService.dispose()
    }
}


//@Preview
//@Composable
//fun AppAndroidPreview() {
//    MainView()
//}