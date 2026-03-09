package com.example.uber.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uber.viewmodel.AuthViewModel
import com.example.uber.ui.screen.Home
import com.example.uber.PantallaLogin
import com.example.uber.PantallaOTP
import com.example.uber.SafetyAlert
import com.example.uber.pantalla_inicial
import com.example.uber.ui.screen.EmailScreen
import com.example.uber.ui.screen.MapScreen
import com.example.uber.ui.screen.ProfileScreen
import com.example.uber.ui.screen.RegisterScreen
import com.example.uber.viewmodel.RideViewModel


@Composable
fun AppNavigation(
    navController: NavHostController,
    authVm: AuthViewModel
){
    val startRoute = if (authVm.isLoggedIn) Routes.HOME else Routes.ONBOARDING

    NavHost(
        navController = navController,
        startDestination = startRoute
    ){

        composable(route = Routes.ONBOARDING) {
            pantalla_inicial(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }
        composable(route = Routes.LOGIN) {
            PantallaLogin(authVm, navController,
                onLoginOk = {
                navController.navigate(Routes.SAFETY){ popUpTo(Routes.LOGIN) { inclusive = true }}
            },
                onLoginOk1 ={navController.navigate((Routes.EMAIL))})
        }

        composable(route = Routes.EMAIL) {
            EmailScreen( authVm,
                onLoginOk = {
                    navController.navigate(Routes.SAFETY) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoRegister = {
                    navController.navigate(Routes.REGISTER) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onBack = {
                    navController.navigate(Routes.LOGIN)})
        }

        composable(route = Routes.REGISTER) {
            RegisterScreen(authVm,
                onRegisterOk = {
                    navController.navigate(Routes.SAFETY) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                })
        }
        composable(route = Routes.OTP) {
            PantallaOTP(authVm,
                onBack = {navController.popBackStack()},
                onSuccess = {
                    navController.navigate(Routes.SAFETY) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                })
        }

        composable(route = Routes.SAFETY){
            SafetyAlert(onNavigateToHome = {navController.navigate(Routes.HOME)})
        }

        composable(route = Routes.HOME){
            Home(onProfileClick =  {navController.navigate(Routes.PROFILE)},
                onMapClick = {navController.navigate(Routes.MAP)})
        }

        composable(route = Routes.MAP) {
            val rideVm: RideViewModel = hiltViewModel()
            MapScreen(
                rideVm = rideVm,
                onRequestRide = {},
                onHistory = {},
                onLogout = {},
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Routes.PROFILE) {
            ProfileScreen(onLogout = {
                authVm.logout()
                navController.navigate(Routes.ONBOARDING) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
    }
}