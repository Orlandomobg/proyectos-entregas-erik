package com.example.ubercloneapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.example.ubercloneapp.viewmodel.RideViewModel

@Composable
fun RequestRideScreen(
    rideVm:     RideViewModel,
    onRideRequested: () -> Unit,  // Ir a pantalla de viaje en curso
    onBack:     () -> Unit
) {
    val context = LocalContext.current
    val userLoc = rideVm.userLocation ?: LatLng(40.4168, -3.7038)
    val dest    = rideVm.destination

    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLoc, 14f)
    }

    Column(Modifier.fillMaxSize()) {

        // ── Barra superior ──
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("← Volver") }
            Spacer(Modifier.weight(1f))
            Text("📍 Elige tu destino", fontWeight = FontWeight.Bold)
        }

        // ── Mapa donde el usuario toca para elegir destino ──
        Box(Modifier.weight(1f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPosition,
                onMapClick = { latLng ->
                    // ↑ Se ejecuta cada vez que el usuario toca el mapa.
                    // latLng = las coordenadas del punto tocado.
                    rideVm.setDestination(latLng, "Destino seleccionado")
                }
            ) {
                // Marcador de origen (verde)
                Marker(
                    state = rememberMarkerState(position = userLoc),
                    title = "Tu ubicación",
                    snippet = "Punto de recogida"
                    // ↑ snippet = texto secundario que aparece al tocar el marcador.
                )

                // Marcador de destino (rojo) — solo si ya eligió uno
                dest?.let {
                    Marker(
                        state = rememberMarkerState(position = it),
                        title = "Destino",
                        snippet = rideVm.destinationName
                    )
                }
            }
        }

        // ── Panel inferior con precio y botón ──
        Surface(
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                if (dest != null) {
                    Text("Destino: ${rideVm.destinationName}")
                    Text("Precio estimado: ${rideVm.estimatedPrice}€",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge)

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            rideVm.requestRide(context = context)
                            // ↑ Inicia la simulación del viaje
                            onRideRequested()
                            // ↑ Navega a la pantalla de viaje en curso
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text("🚗 Pedir viaje",
                            style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    Text("Toca el mapa para elegir tu destino",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}