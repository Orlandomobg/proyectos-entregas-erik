package com.example.ubercloneapp.model

// Representa un viaje guardado en Firestore.
// Cada campo tiene un valor por defecto porque Firestore
// necesita un constructor sin argumentos para deserializar.

data class Ride(
    val firestoreId:  String = "",
    val userId:       String = "",
    // ↑ UID del usuario (de Firebase Auth). Identifica quién pidió el viaje.

    val originLat:    Double = 0.0,
    val originLng:    Double = 0.0,
    val originName:   String = "",
    // ↑ Coordenadas y nombre del punto de recogida.

    val destLat:      Double = 0.0,
    val destLng:      Double = 0.0,
    val destName:     String = "",
    // ↑ Coordenadas y nombre del destino.

    val driverName:   String = "",
    // ↑ Nombre del conductor (simulado — lo generamos nosotros).

    val price:        Double = 0.0,
    // ↑ Precio del viaje (calculado por distancia).

    val status:       String = "completed",
    // ↑ Estado: "completed", "cancelled". En un Uber real habría
    // "searching", "accepted", "in_progress", etc.

    val date:         String = "",
    // ↑ Fecha del viaje como string (ej: "2025-06-15").

    val durationMins: Int    = 0,
    // ↑ Duración estimada en minutos.

    val distanceKm: Double = 0.0
)