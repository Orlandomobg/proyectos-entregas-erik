package com.example.ubercloneapp.ui.screen

import android.Manifest
// ↑ Contiene las constantes de permisos de Android.
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
// ↑ Nos da el Context de Android dentro de un composable.
// Necesario para crear el FusedLocationProviderClient.
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
// ↑ Librería Accompanist: pedir permisos de forma declarativa en Compose.

import com.google.android.gms.location.LocationServices
// ↑ Punto de entrada a los servicios de ubicación de Google.
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
// ↑ GoogleMap, Marker, rememberCameraPositionState...
// Todos los composables de Google Maps.

import com.example.ubercloneapp.viewmodel.RideViewModel

@OptIn(ExperimentalPermissionsApi::class)
// ↑ La API de permisos de Accompanist es experimental.
// @OptIn dice "sé que puede cambiar, la uso conscientemente".

@Composable
fun HomeMapScreen(
    rideVm:       RideViewModel,
    onRequestRide: () -> Unit,   // Ir a pedir viaje
    onHistory:     () -> Unit,   // Ir al historial
    onLogout:      () -> Unit    // Cerrar sesión
) {
    val context = LocalContext.current
    // ↑ Obtener el Context de Android. Necesario para LocationServices.

    // ── Permiso de ubicación ──
    val locationPermission = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    // ↑ Crea un objeto que gestiona el estado del permiso.
    // .status.isGranted = true si el usuario ya lo concedió.
    // .launchPermissionRequest() = muestra el diálogo del sistema.

    // ── Obtener ubicación cuando se concede el permiso ──
    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted) {
            // El permiso está concedido → obtener ubicación
            val fusedClient = LocationServices
                .getFusedLocationProviderClient(context)
            // ↑ FusedLocationProviderClient: la forma moderna y eficiente
            // de obtener la ubicación. Combina GPS + WiFi + sensores.

            try {
                val location = fusedClient.lastLocation
                    .addOnSuccessListener { loc ->
                        // loc puede ser null si el GPS no tiene fix todavía
                        loc?.let {
                            rideVm.updateUserLocation(
                                LatLng(it.latitude, it.longitude)
                            )
                        }
                    }
            } catch (e: SecurityException) {
                // No debería llegar aquí si el permiso está concedido,
                // pero por seguridad lo manejamos.
            }
        }
    }

    // ── Posición de la cámara del mapa ──
    val userLoc = rideVm.userLocation
    val defaultLocation = LatLng(40.4168, -3.7038)
    // ↑ Madrid como posición por defecto si no hay GPS.

    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            userLoc ?: defaultLocation, 15f
        )
        // ↑ Centrar el mapa en la ubicación del usuario con zoom 15.
        // 15f = zoom nivel calle. 10f = ciudad. 5f = país.
    }

    // Mover la cámara cuando se actualiza la ubicación
    LaunchedEffect(userLoc) {
        userLoc?.let {
            cameraPosition.animate(
                com.google.android.gms.maps.CameraUpdateFactory
                    .newLatLngZoom(it, 15f)
            )
        }
    }

    Box(Modifier.fillMaxSize()) {
        if (locationPermission.status.isGranted) {
            // ── MAPA ──
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPosition,
                properties = MapProperties(
                    isMyLocationEnabled = true
                    // ↑ Muestra el punto azul de "tu ubicación" en el mapa.
                    // Solo funciona con permiso de ubicación concedido.
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                    // ↑ Botón que centra el mapa en tu ubicación.
                )
            ) {
                // Marcador en la ubicación del usuario
                userLoc?.let {
                    Marker(
                        state = rememberMarkerState(position = it),
                        title = "Tú estás aquí"
                    )
                }
            }

            // ── Botones flotantes sobre el mapa ──
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onRequestRide,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("¿A dónde vamos?", style = MaterialTheme.typography.titleMedium)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onHistory) {
                        Text("📋 Historial")
                    }
                    OutlinedButton(onClick = onLogout) {
                        Text("🚪 Salir")
                    }
                }
            }
        } else {
            // ── SIN PERMISO: pedir al usuario ──
            Column(
                Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🗺️ Necesitamos tu ubicación",
                    style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                Text("Para mostrarte el mapa y encontrar conductores cerca de ti.")
                Spacer(Modifier.height(24.dp))
                Button(onClick = {
                    locationPermission.launchPermissionRequest()
                    // ↑ Muestra el diálogo del sistema: "¿Permitir acceso a ubicación?"
                }) {
                    Text("Conceder permiso de ubicación")
                }
            }
        }
    }
}