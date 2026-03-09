package com.example.uber.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RideEntity::class],
    // ↑ Lista de TODAS las tablas de la base de datos.
    version = 1
    // ↑ Versión del esquema. Si cambias la estructura de una tabla,
    // incrementa la versión y Room pedirá una migración.
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rideDao(): RideDao
    // ↑ Room genera la implementación. Tú solo declaras el DAO.
}