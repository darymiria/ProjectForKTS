package com.example.projectforkts.favorites.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.projectforkts.main.presentation.RepoItemCard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.icon_star
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import projectforkts.composeapp.generated.resources.icon_filled_star
import projectforkts.composeapp.generated.resources.no_favorites

@Composable
fun FavoritesScreen(
    onRepoClick: (owner: String, repo: String) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel( )
){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit)  {
        viewModel.navigateToDetail.collect { (owner, repo) ->
            onRepoClick(owner, repo)
        }
    }

    if (state.items.isEmpty()){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Icon(
                    painter = painterResource(Res.drawable.icon_star),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.no_favorites),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.items, key = {it.id}) {repo ->
                RepoItemCard(
                    repo = repo,
                    onClick = {viewModel.onRepoClick(repo.owner, repo.name)},
                    trailingContent = {
                        IconButton(onClick = { viewModel.removeFromFavorite(repo)}) {
                            Icon(
                                painter = painterResource(Res.drawable.icon_filled_star),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}