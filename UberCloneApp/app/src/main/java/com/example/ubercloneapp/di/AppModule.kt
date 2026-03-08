package com.example.ubercloneapp.di

import android.content.Context
import androidx.room.Room
import com.example.ubercloneapp.data.local.AppDatabase
import com.example.ubercloneapp.data.local.RideDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
// ↑ Le dice a Hilt: "este archivo contiene recetas para crear objetos".
@InstallIn(SingletonComponent::class)
// ↑ Estas dependencias viven durante TODA la vida de la app.
// SingletonComponent = scope de Application.
// Alternativas: ActivityComponent, ViewModelComponent, etc.
object AppModule {

    @Provides
    // ↑ "Hilt, cuando alguien pida un FirebaseAuth, usa ESTA función".
    @Singleton
    // ↑ Solo crea UNA instancia y la reutiliza siempre.
    fun provideAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage =
        FirebaseStorage.getInstance()


    // ── Room Database ──
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
        // ↑ Hilt inyecta el Context de la Application automáticamente.
        // @ApplicationContext = "dame el contexto global, no el de una Activity".
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "uberclone_db"
            // ↑ Nombre del archivo SQLite en el dispositivo.
            // Se guarda en /data/data/com.example.ubercloneapp/databases/
        ).fallbackToDestructiveMigration()
            // ↑ Si cambias la versión del esquema, BORRA la DB y la recrea.
            // En producción usarías migraciones reales. Para aprender, esto es OK.
            .build()

    @Provides
    @Singleton
    fun provideRideDao(db: AppDatabase): RideDao =
        db.rideDao()
    // ↑ Hilt sabe crear AppDatabase (función anterior).
    // Ahora usa esa instancia para obtener el DAO.
    // Cadena: Context → AppDatabase → RideDao → ViewModel
}
