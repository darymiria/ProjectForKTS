package com.example.projectforkts.main.presentation.detail


import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.draw.clip
import coil3.compose.AsyncImage
import com.example.projectforkts.main.domain.model.FileItem
import com.example.projectforkts.main.domain.model.RepoDetails
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.files_label
import projectforkts.composeapp.generated.resources.forks_label
import projectforkts.composeapp.generated.resources.icon_arrow_back
import projectforkts.composeapp.generated.resources.icon_description
import projectforkts.composeapp.generated.resources.icon_edit
import projectforkts.composeapp.generated.resources.icon_filled_star
import projectforkts.composeapp.generated.resources.icon_folder
import projectforkts.composeapp.generated.resources.icon_share
import projectforkts.composeapp.generated.resources.icon_star
import projectforkts.composeapp.generated.resources.issues_label
import projectforkts.composeapp.generated.resources.language_label
import projectforkts.composeapp.generated.resources.no_readme
import projectforkts.composeapp.generated.resources.retry_button
import projectforkts.composeapp.generated.resources.stars_label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailScreen(
    owner: String,
    repo: String,
    onBack: ()-> Unit,
    onShare: (String) -> Unit,
    onUnauthorized: () -> Unit,
    onCreateIssue: (String) -> Unit,
    viewModel: RepoDetailViewModel = koinViewModel()
){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(owner, repo) {
        viewModel.loadAll(owner, repo)
    }

    LaunchedEffect(Unit) {
        viewModel.unauthorizedEvent.collect{ onUnauthorized()}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(repo) },
                navigationIcon = {IconButton(onClick = onBack){
                    Icon(
                        painter = painterResource(Res.drawable.icon_arrow_back),
                        contentDescription = "Назад")
                }
                },
                actions = {
                    IconButton(onClick = viewModel::toggleFavorite) {
                        if (state.isFavorite){
                                Icon(
                                    painter = painterResource(Res.drawable.icon_filled_star),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                    )
                        }
                        else {
                            Icon(
                                painter = painterResource(Res.drawable.icon_star),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                    }
                    state.details?.let { details -> IconButton(onClick = {onShare(details.htmlUrl)}) {
                        Icon(painter = painterResource(Res.drawable.icon_share), contentDescription = "Поделиться")
                    }
                        IconButton(onClick = {onCreateIssue(details.htmlUrl)}){
                            Icon(painter = painterResource(Res.drawable.icon_edit), contentDescription = null)
                        }
                    }
                }
            )
        }
    ) {innerPadding ->
        when {
            state.isLoading ->{
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            state.error != null ->{
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
                        Button(onClick = {viewModel.loadAll(owner,repo)}){
                            Text(stringResource(Res.string.retry_button))
                        }
                    }
                }
            }
            state.details != null ->{
                RepoDetailContent(
                    state = state,
                    modifier = Modifier.padding(innerPadding)
                )
            }

        }
    }

}

@Composable
private fun RepoDetailContent(
    state: RepoDetailUiState,
    modifier: Modifier = Modifier
){
    val details = state.details!!

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        item{ RepoHeader(details)}
        item{ RepoStats(details)}
        item{RepoReadmeSection(
            readme = state.readme,
            isLoading = state.isReadmeLoading
        )
        }
        if (state.isFilesLoading){
            item{ CircularProgressIndicator(modifier= Modifier.padding(8.dp))}
        } else if (state.files.isNotEmpty()){
            item{
                Text(
                    text = stringResource(Res.string.files_label),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(state.files, key = {it.path}) {file ->
                FileItemRow(file)
            }
        }
    }
}
@Composable
private fun RepoHeader(details: RepoDetails) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = details.avatarUrl,
            contentDescription = null,
            modifier = Modifier.size(48.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = details.fullName, style = MaterialTheme.typography.titleLarge)
            Text(
                text = details.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
@Composable
private fun RepoStats(details: RepoDetails) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        StatItem(label = stringResource(Res.string.stars_label), value = "${details.stars}")
        StatItem(label = stringResource(Res.string.forks_label), value = "${details.forks}")
        StatItem(label = stringResource(Res.string.issues_label), value = "${details.openIssues}")
        details.language?.let {
            StatItem(label = stringResource(Res.string.language_label), value = it)
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium)
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RepoReadmeSection(readme: String?, isLoading: Boolean) {
    Column {
        Text(text = "README", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.size(24.dp))
            readme != null -> Text(
                text = readme,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            )

            else -> Text(
                text = stringResource(Res.string.no_readme),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FileItemRow(file: FileItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
         if (file.type == "dir") {
             Icon(
                 painter = painterResource(Res.drawable.icon_folder),
                 contentDescription = null,
                 tint = MaterialTheme.colorScheme.onSurfaceVariant
             )
         }
         else {
             Icon(
                 painter = painterResource(Res.drawable.icon_description),
                 contentDescription = null,
                 tint = MaterialTheme.colorScheme.onSurfaceVariant
             )
         }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = file.name, style = MaterialTheme.typography.bodyMedium)
    }
}