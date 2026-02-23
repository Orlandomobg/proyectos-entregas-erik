package com.example.uber.navigation

import androidx.compose.runtime.Composable

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uber.AuthViewModel
import androidx.navigation.compose.NavHost
import com.example.uber.PantallaLogin
import com.example.uber.PantallaOTP
import com.example.uber.SafetyAlert
import com.example.uber.pantalla_inicial

@Composable
fun AppNavigation(
    navController: NavHostController,
    authVm: AuthViewModel
){
    val startRoute = Routes.ONBOARDING

    NavHost(
        navController = navController,
        startDestination = startRoute
    ){
        composable(route = Routes.ONBOARDING) {
            // Aquí usamos la función que refactorizamos
            pantalla_inicial(
                onNavigateToLogin = {
                    // El "cable" que conecta con Login
                    navController.navigate(Routes.LOGIN)
                }
            )
        }
        composable(route = Routes.LOGIN) {
            PantallaLogin(authVm, navController)
        }

        composable(route = Routes.OTP) {
            PantallaOTP(authVm, navController)
        }

        composable(route = Routes.SAFETY){
            SafetyAlert(authVm,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate("Safety") {
                        popUpTo("login") { inclusive = true })
        }

    }
}