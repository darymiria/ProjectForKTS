package com.example.projectforkts.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.projectforkts.main.presentation.ErrorState
import org.jetbrains.compose.resources.stringResource
import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.followers_label
import projectforkts.composeapp.generated.resources.following_label
import projectforkts.composeapp.generated.resources.logout_button
import projectforkts.composeapp.generated.resources.repos_label
import androidx.compose.runtime.getValue
import com.example.projectforkts.core.AppStorage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> ErrorState(
                    message = state.error!!,
                    onRetry = viewModel::loadProfile
                )
                state.profile != null -> {
                    AsyncImage(
                        model = state.profile!!.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.profile!!.name ?: state.profile!!.login,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = "@${state.profile!!.login}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${state.profile!!.publicRepos}", style = MaterialTheme.typography.titleMedium)
                            Text(text = stringResource(Res.string.repos_label), style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${state.profile!!.followers}", style = MaterialTheme.typography.titleMedium)
                            Text(text = stringResource(Res.string.followers_label), style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${state.profile!!.following}", style = MaterialTheme.typography.titleMedium)
                            Text(text = stringResource(Res.string.following_label), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = viewModel::logout,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        Text(stringResource(Res.string.logout_button))
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.logoutEvent.collect { onLogout() }
    }
}