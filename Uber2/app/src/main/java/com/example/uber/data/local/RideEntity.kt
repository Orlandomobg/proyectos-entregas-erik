package com.example.uber.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rides")
// ↑ @Entity = esta data class ES una tabla en SQLite.
// tableName = nombre de la tabla. Si no lo pones, usa el nombre de la clase.
data class RideEntity(
    @PrimaryKey
    // ↑ Clave primaria. Cada fila debe tener un ID único.
    // Usamos el ID del documento de Firestore para evitar duplicados.
    val firestoreId: String,
    val userId:      String,
    val driverName:  String,
    val price:       Double,
    val distanceKm:  Double,
    val status:      String,
    val timestamp:   Long = System.currentTimeMillis()
    // ↑ Room trabaja con tipos primitivos. No puede guardar
    // objetos complejos directamente (para eso usa TypeConverters).
)