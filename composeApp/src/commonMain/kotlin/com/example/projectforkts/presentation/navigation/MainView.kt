package com.example.projectforkts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectforkts.ui.GitHubTheme
import com.example.projectforkts.presentation.login.LoginScreen
import com.example.projectforkts.presentation.main.MainScreen
import com.example.projectforkts.presentation.welcome.WelcomeScreen

@Composable
fun MainView() {
    GitHubTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "welcome"
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onLoginClick = { navController.navigate("login") }
                )
            }
            composable("login") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
                )
            }
            composable("main") {
                MainScreen()
            }
        }
    }
}
