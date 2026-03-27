package com.example.projectforkts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.projectforkts.login.presentation.LoginScreen
import com.example.projectforkts.navigation.MainView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MainView(loginScreen = { onLoginSuccess ->
                LoginScreen(onLoginSuccess = onLoginSuccess)
            })
        }
    }
}


//@Preview
//@Composable
//fun AppAndroidPreview() {
//    MainView()
//}