package com.example.uber_carlos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.uber_carlos.navigation.AppNavigation
import com.example.uber_carlos.ui.screens.Home
import com.example.uber_carlos.ui.theme.UberCarlosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            UberCarlosTheme {}

            AppNavigation()

        }
    }
}

