package com.example.ubercloneapp

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ubercloneapp.navigation.AppNavigation
import com.example.ubercloneapp.ui.theme.UberCloneTheme
import com.example.ubercloneapp.viewmodel.AuthViewModel
import com.example.ubercloneapp.viewmodel.RideViewModel
import com.example.ubercloneapp.viewmodel.PaymentViewModel
import com.example.ubercloneapp.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
// ↑ OBLIGATORIO en toda Activity que use Hilt.
// Le dice a Hilt: "inyecta dependencias en esta Activity".
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            androidx.core.app.ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }
        setContent {
            UberCloneTheme {
                // ① NavController — gestiona la pila de pantallas
                val navController = rememberNavController()

                // ② AuthViewModel — login, registro, logout
                //val authVm: AuthViewModel = viewModel()

                // ③ RideViewModel — mapa, viaje, Firestore
                val rideVm: RideViewModel = viewModel()

                // ④ PaymentViewModel — pagos con Stripe ← NUEVO
                val paymentVm: PaymentViewModel = viewModel()

                //SOLO SE NUEVO ESTO
                val authVm: AuthViewModel = hiltViewModel()

                val profileVm: ProfileViewModel = hiltViewModel()

                // ⑤ Lanzar la navegación
                AppNavigation(
                    navController = navController,
                    authVm        = authVm,
                    rideVm        = rideVm,
                    paymentVm     = paymentVm,
                    profileVm     = profileVm
                )
            }
        }
    }
}
// TOMAR EN CUENTA QUE EL HILT SE USA PARA LLAMAR LAS INSTANCIAS DE FIREBASE DIRECTAMENTE