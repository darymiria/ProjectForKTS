package com.example.projectforkts.welcome


import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.projectforkts.ui.GitHubTheme
import org.jetbrains.compose.resources.stringResource
import projectforkts.composeapp.generated.resources.Res
import projectforkts.composeapp.generated.resources.start_button
import projectforkts.composeapp.generated.resources.welcome_description
import projectforkts.composeapp.generated.resources.welcome_title
import projectforkts.composeapp.generated.resources.ic_error
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import projectforkts.composeapp.generated.resources.dark_logo
import projectforkts.composeapp.generated.resources.github_logo
import projectforkts.composeapp.generated.resources.github_logo_dark
import projectforkts.composeapp.generated.resources.light_logo

@Composable
fun WelcomeScreen(onLoginClick: () -> Unit) {
    val isDark = isSystemInDarkTheme()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = (if (isDark) {painterResource(Res.drawable.github_logo_dark)} else {painterResource(Res.drawable.github_logo)}),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.welcome_title),
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.welcome_description),
                color = Color.LightGray,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onLoginClick,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = stringResource(Res.string.start_button),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = 0x20)
@Composable
private fun WelcomeScreenPreview() {
    GitHubTheme {
        WelcomeScreen(onLoginClick = {})
    }
}