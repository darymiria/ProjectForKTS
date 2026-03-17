package com.example.projectforkts.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projectforkts.ui.GitHubTheme
import com.example.projectforkts.main.presentation.MainScreen
import com.example.projectforkts.welcome.WelcomeScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.projectforkts.profile.presentation.ProfileScreen
import org.jetbrains.compose.resources.stringResource
import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.profile_tab
import projectforkts.composeapp.generated.resources.repos_tab
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun MainView(
    startDestination: Any = WelcomeScreen,
    loginScreen: @Composable (onLoginSuccess: () -> Unit) -> Unit,
    onOnboardingComplete: () -> Unit
) {
    GitHubTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable<WelcomeScreen> {
                WelcomeScreen(
                    onLoginClick = {
                        onOnboardingComplete()
                        navController.navigate(LoginScreen)
                    }
                )
            }
            composable<LoginScreen> {
                loginScreen{
                    navController.navigate(MainScreen) {
                        popUpTo<WelcomeScreen> { inclusive = true }
                    }
                }

            }
            composable<MainScreen> {
                MainScreen(
                    onUnauthorized = {
                        navController.navigate(LoginScreen) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreenWithBottomNav(onUnauthorized: () -> Unit) {
    val bottomNavController = rememberNavController()
    val currentDestination by bottomNavController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination?.destination?.hasRoute<ReposScreen>() == true,
                    onClick = {
                        bottomNavController.navigate(ReposScreen) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {},
                    label = { Text(stringResource(Res.string.repos_tab)) }
                )
                NavigationBarItem(
                    selected = currentDestination?.destination?.hasRoute<ProfileScreen>() == true,
                    onClick = {
                        bottomNavController.navigate(ProfileScreen) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {},
                    label = { Text(stringResource(Res.string.profile_tab)) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = ReposScreen,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ReposScreen> {
                MainScreen(onUnauthorized = onUnauthorized)
            }
            composable<ProfileScreen> {
                ProfileScreen(onLogout = onUnauthorized)
            }
        }
    }
}