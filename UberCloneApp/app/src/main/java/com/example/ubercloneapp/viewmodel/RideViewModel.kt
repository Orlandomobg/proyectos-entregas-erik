package com.example.ubercloneapp.viewmodel

// ── Compose ──
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
import com.example.ubercloneapp.model.Ride

//R
import com.example.ubercloneapp.R

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

class RideViewModel : ViewModel() {

    // ── Firebase (singletons) ──
    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()

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

    // ── Nombre del conductor (simulado) ──
    var driverName: String by mutableStateOf("")
        private set

    // ── Historial de viajes ──
    var rideHistory: List<Ride> by mutableStateOf(emptyList())
        private set

    // ── Mensaje de resultado ──
    var resultMsg: String by mutableStateOf("")
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

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
    fun requestRide(context : Context ) {
        if (destination == null || userLocation == null) return

        rideState = RideState.Searching

        // Recalcular precio aquí, donde ya sabemos que userLocation no es null
        val origin = userLocation!!
        val dest = destination!!
        val distKm = haversineDistance(origin, dest)
        estimatedPrice = ((2.5 + distKm * 1.2) * 100).toInt() / 100.0

        android.util.Log.d("RideVM", "Precio en requestRide: $estimatedPrice")

        viewModelScope.launch {
            // ── SIMULACIÓN: buscar conductor ──
            delay(3000)  // Espera 3 segundos (simula búsqueda)

            // Generar conductor aleatorio
            val drivers = listOf("Carlos M.", "Ana R.", "Pedro L.",
                "María G.", "Luis S.")
            driverName = drivers.random()
            // ↑ .random() elige un elemento al azar de la lista.

            val eta = (3..12).random()
            // ↑ Número aleatorio entre 3 y 12 minutos.

            rideState = RideState.DriverAssigned(
                driverName = driverName,
                etaMinutes = eta
            )
            sendLocalNotification(context, "🚗 Conductor asignado", "El conductor $driverName llega en 3 min")

            // ── SIMULACIÓN: el conductor llega y empieza el viaje ──
            delay(5000)  // Espera 5 segundos más
            rideState = RideState.InProgress

            // ── SIMULACIÓN: el viaje termina ──
            val tripDuration = (8..25).random()
            delay((tripDuration * 400).toLong())
            // ↑ Simulamos que el viaje dura unos segundos proporcionales.
            // En una app real, esto sería tracking GPS en tiempo real.

            // Guardar en Firestore y marcar como completado
            saveRideToFirestore(tripDuration)
            rideState = RideState.Completed
            sendLocalNotification(context, "✅ Viaje completado", "Has llegado a tu destino")
        }
    }


    // ═══════════════════════════════════════════
    //  GUARDAR VIAJE EN FIRESTORE
    // ═══════════════════════════════════════════
    private fun saveRideToFirestore(durationMins: Int) {
        val user = auth.currentUser ?: return
        val origin = userLocation ?: return
        val dest   = destination ?: return

        val ride = Ride(
            userId       = user.uid,
            originLat    = origin.latitude,
            originLng    = origin.longitude,
            originName   = "Mi ubicación",
            destLat      = dest.latitude,
            destLng      = dest.longitude,
            destName     = destinationName,
            driverName   = driverName,
            price        = estimatedPrice,
            status       = "completed",
            date         = java.time.LocalDate.now().toString(),
            durationMins = durationMins
        )

        viewModelScope.launch {
            try {
                db.collection("rides").add(ride).await()
                // ↑ Crea un nuevo documento en la colección "rides"
                // con un ID generado automáticamente.
                resultMsg = "✅ Viaje guardado"
            } catch (e: Exception) {
                resultMsg = "❌ Error: ${e.localizedMessage}"
            }
        }
    }

    // ═══════════════════════════════════════════
    //  CARGAR HISTORIAL DEL USUARIO
    // ═══════════════════════════════════════════
    fun loadRideHistory() {
        val user = auth.currentUser ?: return

        viewModelScope.launch {
            try {
                val snapshot = db.collection("rides")
                    .whereEqualTo("userId", user.uid)
                    // ↑ Solo viajes de ESTE usuario
                    .get()
                    .await()

                rideHistory = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Ride::class.java)
                }
            } catch (_: Exception) {
                rideHistory = emptyList()
            }
        }
    }

    // ═══════════════════════════════════════════
    //  RESETEAR para un nuevo viaje
    // ═══════════════════════════════════════════
    fun resetRide() {
        rideState = RideState.Idle
        destination = null
        destinationName = ""
        estimatedPrice = 0.0
        driverName = ""
        resultMsg = ""
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