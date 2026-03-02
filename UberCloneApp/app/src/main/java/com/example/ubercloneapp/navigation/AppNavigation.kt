package com.example.ubercloneapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ubercloneapp.ui.*
import com.example.ubercloneapp.ui.screen.HomeMapScreen
import com.example.ubercloneapp.ui.screen.LoginScreen
import com.example.ubercloneapp.ui.screen.PaymentScreen
import com.example.ubercloneapp.ui.screen.ProfileScreen
import com.example.ubercloneapp.ui.screen.RegisterScreen
import com.example.ubercloneapp.ui.screen.RequestRideScreen
import com.example.ubercloneapp.ui.screen.RideHistoryScreen
import com.example.ubercloneapp.ui.screen.RideInProgressScreen
import com.example.ubercloneapp.viewmodel.AuthViewModel
import com.example.ubercloneapp.viewmodel.RideViewModel
import com.example.ubercloneapp.viewmodel.PaymentViewModel
import com.example.ubercloneapp.viewmodel.ProfileViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    authVm:        AuthViewModel,
    rideVm:        RideViewModel,
    paymentVm:     PaymentViewModel,
    profileVm:     ProfileViewModel    // ← NUEVO
) {
    // ── Pantalla inicial: depende de si hay sesión ──
    val startRoute = if (authVm.isLoggedIn) Routes.HOME_MAP else Routes.LOGIN

    NavHost(navController, startDestination = startRoute) {

        // ── LOGIN ──
        composable(Routes.LOGIN) {
            LoginScreen(
                authVm = authVm,
                onLoginOk = {
                    navController.navigate(Routes.HOME_MAP) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        // ↑ Elimina login de la pila. "Atrás" no vuelve al login.
                    }
                },
                onGoRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        // ── REGISTRO ──
        composable(Routes.REGISTER) {
            RegisterScreen(
                authVm = authVm,
                onRegisterOk = {
                    navController.navigate(Routes.HOME_MAP) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoLogin = { navController.popBackStack() }
            )
        }

        // ── MAPA PRINCIPAL ──
        composable(Routes.HOME_MAP) {
            HomeMapScreen(
                rideVm = rideVm,
                onRequestRide = {
                    navController.navigate(Routes.REQUEST)
                },
                onHistory = {
                    navController.navigate(Routes.HISTORY)
                },
                onLogout = {
                    authVm.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME_MAP) { inclusive = true }
                    }
                },
                onProfile = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        // ── ELEGIR DESTINO ──
        composable(Routes.REQUEST) {
            RequestRideScreen(
                rideVm = rideVm,
                onRideRequested = {
                    navController.navigate(Routes.IN_PROGRESS) {
                        popUpTo(Routes.REQUEST) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── VIAJE EN CURSO ──
        composable(Routes.IN_PROGRESS) {
            RideInProgressScreen(
                rideVm = rideVm,
                onCompleted = {
                    // Al completar viaje → ir a PAGAR (no al mapa)
                    navController.navigate(Routes.PAYMENT) {
                        popUpTo(Routes.IN_PROGRESS) { inclusive = true }
                    }
                }
            )
        }

        // ── PAGO CON STRIPE ── ← NUEVO
        composable(Routes.PAYMENT) {
            PaymentScreen(
                paymentVm   = paymentVm,
                ridePrice   = rideVm.estimatedPrice,
                rideSummary = "Mi ubicación → ${rideVm.destinationName}",
                onPaymentOk = {
                    // Pago OK → guardar viaje en Firestore y volver al mapa
                    rideVm.resetRide()
                    paymentVm.resetPayment()
                    navController.navigate(Routes.HOME_MAP) {
                        popUpTo(Routes.HOME_MAP) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PROFILE){
            ProfileScreen(profileVm = profileVm)
        }



        // ── HISTORIAL ──
        composable(Routes.HISTORY) {
            RideHistoryScreen(
                rideVm = rideVm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}