package com.example.projectforkts.presentation.main

import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.ic_error
import coil3.compose.AsyncImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import com.example.projectforkts.ui.GitHubTheme
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.items, key = { it.id }) { repo ->
            RepoItemCard(repo)
        }
    }
}

@Composable
fun RepoItemCard(repo: RepoItem) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = repo.avatarUrl,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                error = rememberVectorPainter(vectorResource(Res.drawable.ic_error))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = repo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                Text(
                    text = " ${repo.stars}, ${repo.owner}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = 0x20)
@Composable
private fun RepoItemCardPreview() {
    GitHubTheme {
        RepoItemCard(
            repo = RepoItem(
                id = 1,
                name = "KMP PROJECT",
                description = "Проект для КТС на KMP - GitHub",
                stars = 40,
                owner = "Daria"
            )
        )
    }
}