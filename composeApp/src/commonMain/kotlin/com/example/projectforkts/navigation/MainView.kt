package com.example.projectforkts.navigation

import androidx.compose.foundation.layout.padding

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
import com.example.projectforkts.profile.presentation.ProfileScreen
import org.jetbrains.compose.resources.stringResource
import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.profile_tab
import projectforkts.composeapp.generated.resources.repos_tab
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import com.example.projectforkts.core.AppStorage
import com.example.projectforkts.main.presentation.detail.RepoDetailScreen
import com.example.projectforkts.main.presentation.issue.CreateIssueScreen
import com.example.projectforkts.main.presentation.upload.UploadFileScreen

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
                MainScreenWithBottomNav(
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


    Scaffold(
        bottomBar = {
            NavBar(bottomNavController)
        }
    )
    { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = ReposScreen,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ReposScreen> {
                MainScreen(onUnauthorized = onUnauthorized,
                    onRepoClick = {owner, repo ->
                        bottomNavController.navigate(RepoDetailScreen(owner = owner, repo = repo))
                    })
            }
            composable<ProfileScreen> {
                ProfileScreen(onLogout = onUnauthorized)
            }
            composable<RepoDetailScreen>(
                deepLinks = emptyList()
            ) { backStackEntry ->
                val screen = backStackEntry.toRoute<RepoDetailScreen>()
                RepoDetailScreen(
                    owner = screen.owner,
                    repo = screen.repo,
                    onBack = { bottomNavController.popBackStack() },
                    onUnauthorized = onUnauthorized,
                    onShare = { url -> /*  */ },
                    onCreateIssue = { bottomNavController.navigate(CreateIssueScreen(owner = screen.owner, repo = screen.repo)) },
                    onUploadFile = { bottomNavController.navigate(UploadFileScreen(owner = screen.owner, repo = screen.repo)) }
                )

            }
            composable<CreateIssueScreen>{
                backStackEntry ->
                val screen = backStackEntry.toRoute<CreateIssueScreen>()
                CreateIssueScreen(
                    owner = screen.owner,
                    repo = screen.repo,
                    onBack = {bottomNavController.popBackStack()}
                )
            }
            composable< UploadFileScreen>{
                    backStackEntry ->
                val screen = backStackEntry.toRoute<UploadFileScreen>()
                UploadFileScreen(
                    owner = screen.owner,
                    repo = screen.repo,
                    onBack = {bottomNavController.popBackStack()}
                )
            }
        }
    }
}
