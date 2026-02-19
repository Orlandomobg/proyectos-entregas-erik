package com.example.ejercicio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ejercicio.navigation.AppNavigation
import com.example.ejercicio.ui.theme.EjercicioTheme
import com.example.ejercicio.viewmodel.AuthViewModel
import com.example.ejercicio.viewmodel.StoreViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // ① NavController — gestiona la pila de pantallas
                val navController = rememberNavController()

                // ② AuthViewModel — login, registro, logout
                //    Creado a nivel Activity = compartido por Login y Register
                val authVm: AuthViewModel = viewModel()

                // ③ StoreViewModel — productos, carrito, pedidos
                //    Creado a nivel Activity = compartido por TODAS las pantallas
                val storeVm: StoreViewModel = viewModel()

                // ④ Lanzar la navegación pasando TODO
                AppNavigation(navController, storeVm)

            }
        }
    }
}