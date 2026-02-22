package com.example.fakeastore_firebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.fakeastore_firebase.navigation.AppNavigation
import com.example.fakeastore_firebase.viewmodel.AuthViewModel
import com.example.fakeastore_firebase.viewmodel.StoreViewModel

class MainActivity : ComponentActivity() {

    // ④ onCreate = el "arranque" de la Activity.
    // Android llama a esta función automáticamente al abrir la app.
    // "override" = estamos reemplazando el onCreate del padre.
    // "fun" = función. "savedInstanceState: Bundle?" = parámetro nullable.
    // El "?" significa que puede ser null (si es la primera vez que se abre).
    override fun onCreate(savedInstanceState: Bundle?) {

        // ⑤ Llamar al onCreate del padre.
        // OBLIGATORIO. Sin esto, Android no inicializa la Activity.
        // "super" = "la versión de mi padre (ComponentActivity)".
        super.onCreate(savedInstanceState)

        // ⑥ Aquí comienza Compose.
        // setContent { } reemplaza al antiguo setContentView(R.layout.xxx).
        // Todo lo que pongas dentro de las llaves es UI declarativa.
        setContent {

            // ⑦ MaterialTheme envuelve toda la app.
            // Proporciona colores, tipografías y formas a TODOS los
            // componentes hijos. Sin esto, Text, Button, Card etc.
            // no tendrían estilos coherentes.
            MaterialTheme {

                // ⑧ Crear el NavController AQUÍ ARRIBA.
                // Nivel Activity = existe ANTES que cualquier pantalla.
                // "val" = no va a cambiar (siempre el mismo controller).
                val navController = rememberNavController()

                // ⑨ Crear los ViewModels AQUÍ ARRIBA.
                // Al crearlos a nivel de Activity, TODAS las pantallas
                // que lo reciban compartirán la MISMA instancia.
                val storeVm: StoreViewModel = viewModel()
                val authVm: AuthViewModel = viewModel()

                // ⑩ Lanzar la navegación con los viewmodels.
                AppNavigation(
                    navController = navController,
                    authVm        = authVm,
                    storeVm       = storeVm
                )
            }
        }
    }
}