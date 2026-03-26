package com.example.projectforkts.main.presentation

import androidx.compose.foundation.Image
import projectforkts.composeapp.generated.resources.Res
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.projectforkts.ui.GitHubTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import coil3.compose.AsyncImage
import com.example.projectforkts.main.domain.RepoItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import projectforkts.composeapp.generated.resources.cached_data_banner
import projectforkts.composeapp.generated.resources.repos_not_found
import projectforkts.composeapp.generated.resources.retry_button
import projectforkts.composeapp.generated.resources.search
import projectforkts.composeapp.generated.resources.stars

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onUnauthorized: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val pullRefreshState = rememberPullToRefreshState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleIndex >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !state.isLoading && state.hasNextPage) {
            viewModel.loadNextPage()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.unauthorizedEvent.collect {
            onUnauthorized()
        }
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::refresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = viewModel::onQueryChanged,
                    label = { Text(stringResource(Res.string.search)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            if (state.isFromCache) {
                item { CacheBanner() }
            }
            if (state.isLoading && state.items.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            if (state.error != null && state.items.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorState(message = state.error!!, onRetry = viewModel::retry)
                    }
                }
            }
            if (state.items.isEmpty() && !state.isLoading && state.error == null) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.stars),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                        Text(text = stringResource(Res.string.repos_not_found))
                    }
                }
            }
            items(state.items, key = { it.id }) { repo ->
                RepoItemCard(repo)
            }
            if (state.isLoading && state.items.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
    }
}
        @Composable
        fun ErrorState(
            message: String,
            onRetry: () -> Unit,
            modifier: Modifier = Modifier
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = message, color = MaterialTheme.colorScheme.error)
                Button(onClick = onRetry) { Text(stringResource(Res.string.retry_button)) }
            }
        }

        @Composable
        fun CacheBanner() {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = stringResource(Res.string.cached_data_banner),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
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
                        modifier = Modifier.size(48.dp)
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
                            text = "★${repo.stars}, ${repo.owner}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        repo.language?.let {
                            Text(
                                text = "$it",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }


//    @Preview(showBackground = true)
//    @Preview(uiMode = 0x20)
//    @Composable
//    private fun RepoItemCardPreview() {
//        GitHubTheme {
//            RepoItemCard(
//                repo = RepoItem(
//                    id = 1,
//                    name = "KMP PROJECT",
//                    description = "Проект для КТС на KMP - GitHub",
//                    language = "Kotlin",
//                    stars = 40,
//                    owner = "Daria"
//                )
//            )
//        }
//    }

//    @Preview(showBackground = true)
//    @Preview(uiMode = 0x20)
//    @Composable
//    private fun ErrorPreview() {
//        GitHubTheme {
//            ErrorState(
//                message = "Нет подключения к интернету",
//                onRetry = {}
//            )
//        }
//    }

//    @Preview(showBackground = true)
//    @Preview(uiMode = 0x20)
//    @Composable
//    private fun CachePreview() {
//        GitHubTheme {
//            CacheBanner()
//        }
//    }

