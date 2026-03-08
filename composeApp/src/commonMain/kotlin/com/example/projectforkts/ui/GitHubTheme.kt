package com.example.projectforkts.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.projectforkts.ui.GitHubColors

@Composable
fun GitHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = GitHubColors.DarkPrimary,
            onPrimary = GitHubColors.DarkOnPrimary,
            secondary = GitHubColors.DarkSecondary,
            background = GitHubColors.DarkBackground,
            surface = GitHubColors.DarkSurface,
            onBackground = GitHubColors.DarkTextPrimary,
            onSurface = GitHubColors.DarkTextPrimary,
            error = Color(0xFFF85149),
            onError = Color.White
        )
    } else {
        lightColorScheme(
            primary = GitHubColors.LightPrimary,
            onPrimary = GitHubColors.LightOnPrimary,
            secondary = GitHubColors.LightSecondary,
            background = GitHubColors.LightBackground,
            surface = GitHubColors.LightSurface,
            onBackground = GitHubColors.LightTextPrimary,
            onSurface = GitHubColors.LightTextPrimary,
            error = Color(0xFFCF222E),
            onError = Color.White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}