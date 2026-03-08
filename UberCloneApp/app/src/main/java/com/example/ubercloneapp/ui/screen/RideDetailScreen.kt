package com.example.ubercloneapp.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ubercloneapp.model.Ride
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// ═══════════════════════════════════════════
//  PANTALLA DE DETALLE DEL VIAJE
// ═══════════════════════════════════════════
// Destino del Deep Link: uberclone://ride/{rideId}
// Carga UN viaje de Firestore por su document ID.
// También accesible desde el historial al tocar un viaje.

@Composable
fun RideDetailScreen(rideId: String?) {
    // ↑ Puede ser null si el Deep Link viene mal formado.
    // Siempre manejar el caso null para evitar crashes.

    var ride      by remember { mutableStateOf<Ride?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error     by remember { mutableStateOf<String?>(null) }
    val context   = LocalContext.current

    // ── Cargar el viaje de Firestore al entrar ──
    LaunchedEffect(rideId) {
        // LaunchedEffect se ejecuta UNA VEZ cuando rideId cambia.
        if (rideId == null) {
            error = "ID de viaje no válido"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val doc = FirebaseFirestore.getInstance()
                .collection("rides")
                .document(rideId)
                // ↑ A diferencia del historial (que usa .whereEqualTo),
                // aquí accedemos a UN documento directamente por ID.
                // Es más rápido: no hay query, solo lectura directa.
                .get()
                .await()

            ride = doc.toObject(Ride::class.java)
            // ↑ Convierte el documento en nuestro data class Ride.
            // Si el doc no existe, toObject devuelve null.

            if (ride == null) error = "Viaje no encontrado"

        } catch (e: Exception) {
            error = e.localizedMessage ?: "Error al cargar el viaje"
        } finally {
            isLoading = false
        }
    }

    // ── UI ──
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📋 Detalle del viaje",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(24.dp))

        when {
            isLoading -> CircularProgressIndicator()

            error != null -> {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }

            ride != null -> {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp)) {
                        Text("🚗 Conductor: ${ride!!.driverName}",
                            style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))
                        Text("📍 Distancia: ${"%.1f".format(ride!!.distanceKm)} km")
                        Text("💰 Precio: ${"%.2f".format(ride!!.price)} €")
                        Text("⏱ Duración: ${ride!!.durationMins} min")
                        Text("📊 Estado: ${ride!!.status}")
                        Text("📅 Fecha: ${ride!!.date}")
                        Spacer(Modifier.height(8.dp))
                        Text("📌 De: ${ride!!.originName}")
                        Text("📌 A: ${ride!!.destName}")
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Botón para compartir el Deep Link
                OutlinedButton(
                    onClick = { shareRideLink(context, rideId!!) }
                ) {
                    Text("📤 Compartir viaje")
                }
            }
        }
    }
}

// ═══════════════════════════════════════════
//  COMPARTIR LINK DEL VIAJE
// ═══════════════════════════════════════════
// Genera un Deep Link y abre el menú de compartir del sistema.

fun shareRideLink(context: Context, rideId: String) {
    val link = "uberclone://ride/$rideId"
    // ↑ Este URI es el que Android intercepta para abrir
    // nuestra app directamente en RideDetailScreen.

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "¡Mira mi viaje en UberClone! $link")
    }
    context.startActivity(Intent.createChooser(intent, "Compartir viaje"))
    // ↑ createChooser muestra: WhatsApp, Telegram, email, copiar...
}