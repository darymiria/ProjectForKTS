package com.example.projectforkts.login.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

@Composable
fun LoginAuthEffectHandler(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current


    val authLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = result.data ?: return@rememberLauncherForActivityResult
        val authResponse = AuthorizationResponse.fromIntent(intent)
        val authException = AuthorizationException.fromIntent(intent)
        when {
            authResponse != null -> viewModel.onAuthCodeReceived(authResponse.createTokenExchangeRequest())
            authException != null -> viewModel.onAuthCodeFailed(authException)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.authSuccessFlow.collect {
            onLoginSuccess()
        }
    }
}