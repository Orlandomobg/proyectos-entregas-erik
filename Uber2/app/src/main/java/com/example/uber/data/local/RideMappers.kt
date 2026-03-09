package com.example.uber.data.local

import com.example.uber.model.Ride

// ═══════════════════════════════════════════
//  FUNCIONES DE MAPEO
// ═══════════════════════════════════════════
// Ride = modelo de Firestore (tiene TODOS los campos).
// RideEntity = tabla de Room (tiene solo los campos que necesitamos offline).
// Estas funciones convierten entre ambos.

fun Ride.toEntity(): RideEntity = RideEntity(
    firestoreId = firestoreId,
    // ↑ Usamos el ID de Firestore como PrimaryKey de Room.
    // Así, si insertamos el mismo viaje dos veces, REPLACE lo sobreescribe.
    userId      = userId,
    driverName  = driverName,
    price       = price,
    distanceKm  = distanceKm,
    status      = status,
    timestamp   = System.currentTimeMillis()
)

fun RideEntity.toRide(): Ride = Ride(
    firestoreId = firestoreId,
    userId      = userId,
    driverName  = driverName,
    price       = price,
    distanceKm  = distanceKm,
    status      = status
    // ↑ Los campos que no tenemos en Room (originName, destName...)
    // quedan con sus valores por defecto ("").
    // Para una caché completa, añadirías más campos a RideEntity.
)