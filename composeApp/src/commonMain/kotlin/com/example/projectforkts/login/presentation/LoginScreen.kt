package com.example.projectforkts.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.projectforkts.ui.GitHubTheme
import org.jetbrains.compose.resources.painterResource

import projectforkts.composeapp.generated.resources.ic_error
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.dark_logo
import projectforkts.composeapp.generated.resources.enter_to_sistem
import projectforkts.composeapp.generated.resources.light_logo
import projectforkts.composeapp.generated.resources.login
import projectforkts.composeapp.generated.resources.password
import projectforkts.composeapp.generated.resources.login_button

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginUiEvent.LoginSuccess -> {
                    keyboardController?.hide()
                    onLoginSuccess()
                }
            }
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

            LoginFields(
                username = state.username,
                password = state.password,
                error = state.error,
                isButtonActive = state.isLoginButtonActive,
                onUsernameChanged = viewModel::onUsernameChanged,
                onPasswordChanged = viewModel::onPasswordChanged,
                onLoginClick = viewModel::onLoginClick
            )
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

@Composable
private fun LoginFields(
    username: String,
    password: String,
    error: String?,
    isButtonActive: Boolean,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChanged,
                label = { Text(stringResource(Res.string.login)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = error != null,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done,
                    autoCorrectEnabled = false,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isButtonActive) {
                            keyboardController?.hide()
                            onLoginClick()
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChanged,
                label = { Text(stringResource(Res.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = error != null,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    autoCorrectEnabled = false,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isButtonActive) {
                            keyboardController?.hide()
                            onLoginClick()
                        }
                    }
                )
            )

            if (error != null) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onLoginClick,
                enabled = isButtonActive,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = stringResource(Res.string.login_button),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }


@Preview(showBackground = true)
@Composable
private fun LoginScreenErrorPreview() {
    GitHubTheme {
        LoginFields(
            username = "admin",
            password = "wrong",
            error = "Неверный логин или пароль",
            isButtonActive = true,
            onUsernameChanged = {},
            onPasswordChanged = {},
            onLoginClick = {}
        )
    }
}

