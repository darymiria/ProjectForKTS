package com.example.projectforkts.login.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectforkts.ui.GitHubTheme
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.dark_logo
import projectforkts.composeapp.generated.resources.enter_to_sistem
import projectforkts.composeapp.generated.resources.light_logo
import projectforkts.composeapp.generated.resources.login_button

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState(initial = false)

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
        viewModel.openAuthPageFlow.collect { intent ->
            authLauncher.launch(intent)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.authSuccessFlow.collect {
            onLoginSuccess()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginHeader()

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = viewModel::openLoginPage,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.login_button),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginHeader() {
    val isDark = isSystemInDarkTheme()

    Image(
        painter = (if (isDark) {painterResource(Res.drawable.dark_logo)} else {painterResource(Res.drawable.light_logo)}),
        contentDescription = null,
        modifier = Modifier.size(400.dp)
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = stringResource(Res.string.enter_to_sistem),
        style = MaterialTheme.typography.headlineSmall
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    GitHubTheme {
        LoginHeader()
    }
}




