package com.example.projectforkts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectforkts.ui.GitHubTheme
import com.example.projectforkts.login.presentation.LoginScreen
import com.example.projectforkts.main.presentation.MainScreen
import com.example.projectforkts.welcome.WelcomeScreen

@Composable
fun MainView() {
    GitHubTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = WelcomeScreen
        ) {
            composable<WelcomeScreen> {
                WelcomeScreen(
                    onLoginClick = { navController.navigate(LoginScreen) }
                )
            }
            composable<LoginScreen> {
                LoginScreen(onLoginSuccess = {
                    navController.navigate(MainScreen) {
                        popUpTo<WelcomeScreen> { inclusive = true }
                    }
                }
                )
            }
            composable<MainScreen> {
                MainScreen()
            }
        }
    }
}
