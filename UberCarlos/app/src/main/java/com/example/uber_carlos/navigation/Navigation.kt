package com.example.uber_carlos.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uber_carlos.ui.screens.Home
import com.example.uber_carlos.ui.screens.LoginScreen
import com.example.uber_carlos.ui.screens.MyScreen
import com.example.uber_carlos.ui.screens.SafetyAndRespect
import com.example.uber_carlos.ui.screens.ScreenCode
import com.example.uber_carlos.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val startDest = if (authViewModel.isLoggedIn) "Home" else "onboarding"

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable("onboarding") {
            MyScreen(onNavigateToLogin = { navController.navigate("login") })
        }

        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToCode = { navController.navigate("Code") }
            )
        }

        composable("Code") {
            ScreenCode(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate("Safety") {
                        popUpTo("login") { inclusive = true } // Limpiamos para que no pueda volver al login
                    }
                }
            )
        }

        composable("Safety") {
            SafetyAndRespect(onNavigateToHome = {
                navController.navigate("Home") {
                    popUpTo("Safety") { inclusive = true }
                }
            })
        }

        composable("Home") {
            Home(viewModel = authViewModel, onLogout = {
                navController.navigate("login") {
                    popUpTo("Home") { inclusive = true }
                }
            })
        }
    }
}

