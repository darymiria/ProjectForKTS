package com.example.projectforkts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter

@Composable
fun WelcomeScreen(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val painter = rememberAsyncImagePainter(
            model = "https://img.freepik.com/premium-photo/cute-astronaut-star-space-isolated-transparent-background-watercolor-illustration-outer-space-with-stars_687490-1581.jpg?semt=ais_hybrid&w=740", // замените на реальный URL
            onError = { }
        )
        AsyncImage(
            model = "https://img.freepik.com/premium-photo/cute-astronaut-star-space-isolated-transparent-background-watercolor-illustration-outer-space-with-stars_687490-1581.jpg?semt=ais_hybrid&w=740",
            contentDescription = " ",
            modifier = Modifier.size(400.dp),
            onError = {},
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Добро пожаловать!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onLoginClick) {
            Text("Начать")
        }
    }
}