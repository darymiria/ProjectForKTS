package com.example.projectforkts.main.presentation.upload

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.commit_message_label
import projectforkts.composeapp.generated.resources.create_issue_button
import projectforkts.composeapp.generated.resources.file_path_label
import projectforkts.composeapp.generated.resources.icon_arrow_back
import projectforkts.composeapp.generated.resources.issue_title_label
import projectforkts.composeapp.generated.resources.select_file
import projectforkts.composeapp.generated.resources.upload_button
import projectforkts.composeapp.generated.resources.upload_file_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadFileScreen(
    owner: String,
    repo: String,
    onBack: () -> Unit,
    viewModel: UploadFileViewModel = koinViewModel()
){
    val state by viewModel.state.collectAsState()

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        uri -> uri ?: return@rememberLauncherForActivityResult
    }

    LaunchedEffect(Unit) {
        viewModel.successEvent.collect { onBack() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(stringResource(Res.string.upload_file_title))},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_arrow_back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { filePicker.launch("*/*")}
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(Res.drawable.icon_arrow_back),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = state.fileName.ifBlank { stringResource(Res.string.select_file) }
                    )
                }
            }
            OutlinedTextField(
                value = state.filePath,
                onValueChange = viewModel::onFilePathChanged,
                label = {Text(stringResource(Res.string.file_path_label))},
                singleLine = true,
                placeholder = {Text("src/main/kotlin/NewFile.kt")},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = state.commitMessage,
                onValueChange = viewModel::onCommitMessageChanged,
                label = {Text(stringResource(Res.string.commit_message_label))},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            state.error?.let{
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {viewModel.upload(owner,repo)},
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ){
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(Res.string.upload_button))
                }
            }
        }
    }

}