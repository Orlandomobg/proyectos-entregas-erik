package com.example.uber.viewmodel

import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat

// ── ViewModel + Coroutines ──
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
// ↑ delay() pausa una coroutine X milisegundos.
// Lo usamos para SIMULAR que el conductor tarda en llegar.
import kotlinx.coroutines.tasks.await

// ── Firebase ──
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// ── Google Maps ──
import com.google.android.gms.maps.model.LatLng
// ↑ Clase que representa un punto en el mapa (latitud, longitud).

// ── Modelo ──
import com.example.uber.model.Ride

//R
import com.example.uber.R
import com.example.uber.data.local.RideDao
import com.example.uber.data.local.RideEntity
import com.example.uber.data.local.toEntity
import com.example.uber.data.local.toRide
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.time.LocalDate

// ═══════════════════════════════════════════
//  ESTADOS DEL VIAJE
// ═══════════════════════════════════════════
// El viaje pasa por estas fases, como una máquina de estados:
// Idle → Searching → DriverAssigned → InProgress → Completed

sealed interface RideState {
    data object Idle           : RideState
    // ↑ No hay viaje activo. El usuario está en el mapa.

    data object Searching      : RideState
    // ↑ Buscando conductor (simulado con delay).

    data class DriverAssigned(
        val driverName: String,
        val etaMinutes: Int
    ) : RideState
    // ↑ Se encontró conductor. Muestra nombre y tiempo estimado.

    data object InProgress     : RideState
    // ↑ El viaje está en curso.

    data object Completed      : RideState
    // ↑ Viaje terminado. Se guardó en Firestore.
}
@HiltViewModel
class RideViewModel @Inject constructor(
    private val rideDao: RideDao,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    // ── Estado del viaje (observable por las pantallas) ──
    var rideState: RideState by mutableStateOf(RideState.Idle)
        private set

    // ── Ubicación actual del usuario ──
    var userLocation: LatLng? by mutableStateOf(null)
        private set
    // ↑ null hasta que obtengamos la ubicación GPS.
    // Cuando se actualice, el mapa se mueve automáticamente.

    // ── Destino elegido por el usuario ──
    var destination: LatLng? by mutableStateOf(null)
        private set

    var destinationName: String by mutableStateOf("")
        private set

    // ── Precio estimado ──
    var estimatedPrice: Double by mutableStateOf(0.0)
        private set

    var estimatedDistanceKm: Double by mutableStateOf(0.0)
        private set
    // ── Nombre del conductor (simulado) ──
    var driverName: String by mutableStateOf("")
        private set

    // ── Historial de viajes ──
    var rideHistory: List<Ride> by mutableStateOf(emptyList())
        private set

    // ── Mensaje de resultado ──
    var resultMsg: String by mutableStateOf("")
        private set

    var lastRideId: String by mutableStateOf("")
        private set

    // ═══════════════════════════════════════════
    //  ACTUALIZAR UBICACIÓN
    // ═══════════════════════════════════════════
    // La pantalla del mapa llama a esta función cuando obtiene
    // la ubicación del GPS.
    fun updateUserLocation(latLng: LatLng) {
        userLocation = latLng
    }

    // ═══════════════════════════════════════════
    //  ELEGIR DESTINO
    // ═══════════════════════════════════════════
    // El usuario toca un punto en el mapa o escribe una dirección.
    fun setDestination(latLng: LatLng, name: String) {
        destination = latLng
        destinationName = name

        android.util.Log.d("RideVM", "userLocation al setDestination: $userLocation")

        // Calcular precio estimado basado en distancia
        // (fórmula simplificada para fines didácticos)
        val origin = userLocation ?: return
        val distKm = haversineDistance(origin, latLng)
        estimatedPrice = ((2.5 + distKm * 1.2) * 100).toInt() / 100.0
        // ↑ Precio base 2.5€ + 1.2€/km. Redondeamos a 2 decimales.

        android.util.Log.d("RideVM", "Precio calculado: $estimatedPrice")
    }

    // ═══════════════════════════════════════════
    //  PEDIR VIAJE (simulado)
    // ═══════════════════════════════════════════

    private fun sendLocalNotification(context: Context, title: String, body: String) {
        val channelId = "uber_rides"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .build()

        //manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    // ═══════════════════════════════════════════
    //  UTILIDAD: Distancia entre dos puntos (Haversine)
    // ═══════════════════════════════════════════
    // Fórmula matemática que calcula la distancia entre dos
    // coordenadas GPS sobre la esfera terrestre.
    private fun haversineDistance(a: LatLng, b: LatLng): Double {
        val R = 6371.0  // Radio de la Tierra en km
        val dLat = Math.toRadians(b.latitude - a.latitude)
        val dLng = Math.toRadians(b.longitude - a.longitude)
        val sinLat = Math.sin(dLat / 2)
        val sinLng = Math.sin(dLng / 2)
        val h = sinLat * sinLat +
                Math.cos(Math.toRadians(a.latitude)) *
                Math.cos(Math.toRadians(b.latitude)) *
                sinLng * sinLng
        return 2 * R * Math.asin(Math.sqrt(h))
    }
}