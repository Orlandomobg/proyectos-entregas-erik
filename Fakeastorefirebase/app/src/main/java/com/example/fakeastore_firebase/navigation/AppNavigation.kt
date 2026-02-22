package com.example.fakeastore_firebase.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fakeastore_firebase.ui.screens.CartScreen
import com.example.fakeastore_firebase.ui.screens.CatalogScreen
import com.example.fakeastore_firebase.ui.screens.ConfirmScreen
import com.example.fakeastore_firebase.ui.screens.DetailScreen
import com.example.fakeastore_firebase.ui.screens.LoginScreen
import com.example.fakeastore_firebase.ui.screens.RegisterScreen
import com.example.fakeastore_firebase.viewmodel.AuthViewModel
import com.example.fakeastore_firebase.viewmodel.StoreViewModel
@Composable
fun AppNavigation(
    navController: NavHostController,
    authVm: AuthViewModel,
    storeVm: StoreViewModel
) {
    // ── Decidir la pantalla inicial ──
    val startRoute = if (authVm.isLoggedIn) Routes.CATALOG else Routes.LOGIN

    NavHost(navController = navController, startDestination = startRoute) {

        // ── LOGIN ──
        composable(route = Routes.LOGIN) {
            LoginScreen(
                authVm   = authVm,
                onLoginOk = {
                    navController.navigate(Routes.CATALOG) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // ── REGISTRO ──
        composable(route = Routes.REGISTER) {
            RegisterScreen(
                authVm    = authVm,
                onRegisterOk = {
                    navController.navigate(Routes.CATALOG) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoLogin = { navController.popBackStack() }
            )
        }

        // ── CATÁLOGO ──
        composable(route = Routes.CATALOG) {
            CatalogScreen(
                viewModel      = storeVm,
                onProductClick = { id ->
                    navController.navigate(Routes.detailRoute(id))
                },
                onCartClick = {
                    navController.navigate(Routes.CART)
                },
                onLogout = {
                    authVm.logout()
                    // Al cerrar sesión → ir al login limpiando el historial
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── DETALLE ──
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { entry ->
            val productId = entry.arguments?.getInt("productId") ?: 0
            DetailScreen(
                productId = productId,
                viewModel = storeVm,
                onBack     = { navController.popBackStack() },
                onGoToCart = { navController.navigate(Routes.CART) }
            )
        }

        // ── CARRITO ──
        composable(route = Routes.CART) {
            CartScreen(
                viewModel = storeVm,
                onBack    = { navController.popBackStack() },
                onConfirm = {
                    storeVm.saveOrderToFirestore()
                }
            )
        }
    }
}