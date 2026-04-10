package com.example.projectforkts.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.projectforkts.favorites.presentation.FavoritesScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.favorites_tab
import projectforkts.composeapp.generated.resources.icon_star
import projectforkts.composeapp.generated.resources.profile_tab
import projectforkts.composeapp.generated.resources.repos_tab

@Composable
fun NavBar(navController : NavHostController){
    val currentDestination by navController.currentBackStackEntryAsState()
    NavigationBar  {
        NavigationBarItem(
        selected = currentDestination?.destination?.hasRoute<ReposScreen>() == true,
        onClick = {
            navController.navigate(ReposScreen) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = {},
        label = { Text(stringResource(Res.string.repos_tab)) }
    )
        NavigationBarItem(
            selected = currentDestination?.destination?.hasRoute<FavoritesScreen>() == true,
            onClick = {
                navController.navigate(FavoritesScreen) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { },
            label = { Text(stringResource(Res.string.favorites_tab)) }
        )
        NavigationBarItem(
        selected = currentDestination?.destination?.hasRoute<ProfileScreen>() == true,
        onClick = {
            navController.navigate(ProfileScreen) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = {},
        label = { Text(stringResource(Res.string.profile_tab)) }
    )
}
}
