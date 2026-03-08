package com.example.ubercloneapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
// ↑ DAO = Data Access Object. Define las operaciones de la tabla.
// Room genera la implementación SQL automáticamente.
interface RideDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // ↑ Si ya existe una fila con el mismo PrimaryKey → la reemplaza.
    // Así al sincronizar con Firestore no hay duplicados.
    suspend fun insertAll(rides: List<RideEntity>)

    @Query("SELECT * FROM rides WHERE userId = :uid ORDER BY timestamp DESC")
    // ↑ Consulta SQL pura. :uid es un parámetro de la función.
    // Room verifica la SQL en COMPILACIÓN — si hay un typo, no compila.
    fun getRidesByUser(uid: String): Flow<List<RideEntity>>
    // ↑ Flow = stream reactivo. Cuando insertas nuevas filas,
    // el Flow emite automáticamente la lista actualizada.
    // Compose la observa con collectAsState().

    @Query("DELETE FROM rides")
    suspend fun clearAll()
    // ↑ Útil para logout: borrar la caché local.
}