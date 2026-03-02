package com.example.ubercloneapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}