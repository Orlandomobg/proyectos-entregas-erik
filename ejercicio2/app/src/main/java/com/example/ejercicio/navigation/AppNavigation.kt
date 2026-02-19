package com.example.ejercicio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ejercicio.viewmodel.AuthViewModel
import com.example.ejercicio.viewmodel.StoreViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    authVm: AuthViewModel,     // ← NUEVO
    storeVm:       StoreViewModel
) {
    // ── Decidir la pantalla inicial ──
    // Si hay sesión activa → catálogo. Si no → login.
    val startRoute = if (authVm.isLoggedIn) Routes.CATALOG else Routes.LOGIN

    NavHost(navController = navController, startDestination = startRoute) {

        // ── LOGIN ──
        composable(route = Routes.LOGIN) {
            LoginScreen(
                authVm   = authVm,
                onLoginOk = {
                    // Al logear OK → ir al catálogo limpiando la pila
                    // para que "atrás" no vuelva al login
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

        // ── CARRITO (ahora guarda en Firestore) ──
        composable(route = Routes.CART) {
            CartScreen(
                viewModel = storeVm,
                onBack    = { navController.popBackStack() },
                onConfirm = {
                    // Guardar el pedido en Firestore
                    storeVm.saveOrderToFirestore()
                }
            )
        }
    }
}