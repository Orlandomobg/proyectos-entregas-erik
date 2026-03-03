package com.example.ubercloneapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.ubercloneapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

// ═══════════════════════════════════════════
//  SERVICIO FCM
// ═══════════════════════════════════════════
// Este servicio se ejecuta en BACKGROUND.
// Android lo despierta automáticamente cuando llega un mensaje FCM.
// No necesitas lanzarlo tú — FCM lo hace.

class UberFcmService : FirebaseMessagingService() {

    // ── Se llama cuando llega un NUEVO TOKEN ──
    // Ocurre al instalar la app, al borrar datos, o cuando
    // FCM decide rotar el token (por seguridad).
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token: $token")
        // ↑ En una app real, enviarías este token a tu servidor
        // para asociarlo al userId en Firestore.
        // Así el servidor sabe a qué dispositivo enviar notificaciones.
    }

    // ── Se llama cuando llega un MENSAJE ──
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // RemoteMessage puede tener:
        // - notification: título + cuerpo (se muestra sola si app cerrada)
        // - data: mapa clave-valor (para lógica personalizada)

        val title = message.notification?.title ?: "UberClone"
        val body  = message.notification?.body  ?: "Tienes una actualización"

        showNotification(title, body)
    }

    // ═══════════════════════════════════════════
    //  MOSTRAR NOTIFICACIÓN LOCAL
    // ═══════════════════════════════════════════
    private fun showNotification(title: String, body: String) {
        val channelId = "uber_rides"
        val manager = getSystemService(NotificationManager::class.java)

        // Desde Android 8 (Oreo), las notificaciones NECESITAN un canal.
        // Sin canal = la notificación no aparece.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Viajes",       // Nombre visible en ajustes
                NotificationManager.IMPORTANCE_HIGH
                // ↑ HIGH = suena, vibra y aparece como heads-up.
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            // ↑ Icono que aparece en la barra de estado.
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            // ↑ La notificación desaparece al tocarla.
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
        // ↑ ID único para cada notificación (basado en timestamp).
    }
}