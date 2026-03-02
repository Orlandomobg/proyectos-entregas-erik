package com.example.ubercloneapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ubercloneapp.viewmodel.RideState
import com.example.ubercloneapp.viewmodel.RideViewModel

@Composable
fun RideInProgressScreen(
    rideVm:     RideViewModel,
    onCompleted: () -> Unit   // Cuando el viaje termina → volver al mapa
) {
    val state = rideVm.rideState

    // Cuando el viaje se completa, navegar de vuelta
    LaunchedEffect(state) {
        if (state is RideState.Completed) {
            // Pequeña pausa para que el usuario vea el mensaje
            kotlinx.coroutines.delay(2000)
            onCompleted()
            //rideVm.resetRide()
        }
    }

    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── UI diferente según el estado del viaje ──
        // "when" = switch de Kotlin. Evalúa el estado actual
        // y muestra la UI correspondiente.
        when (state) {
            is RideState.Searching -> {
                CircularProgressIndicator(Modifier.size(64.dp))
                Spacer(Modifier.height(24.dp))
                Text("🔍 Buscando conductor...",
                    style = MaterialTheme.typography.headlineSmall)
                Text("Esto puede tardar unos segundos",
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            is RideState.DriverAssigned -> {
                Text("🚗", fontSize = 64.sp)
                Spacer(Modifier.height(16.dp))
                Text("¡Conductor encontrado!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))

                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Conductor: ${state.driverName}",
                            fontWeight = FontWeight.Bold)
                        Text("Llega en ~${state.etaMinutes} min")
                        Text("Precio: ${rideVm.estimatedPrice}€")
                    }
                }
            }

            is RideState.InProgress -> {
                Text("🛣️", fontSize = 64.sp)
                Spacer(Modifier.height(16.dp))
                Text("Viaje en curso...",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold)
                Text("Destino: ${rideVm.destinationName}")
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(Modifier.fillMaxWidth())
                // ↑ Barra de progreso indeterminada (se mueve sola).
            }

            is RideState.Completed -> {
                Text("✅", fontSize = 64.sp)
                Spacer(Modifier.height(16.dp))
                Text("¡Has llegado!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold)
                Text("Viaje guardado en tu historial",
                    textAlign = TextAlign.Center)
            }

            else -> {
                Text("Esperando...")
            }
        }
    }
}