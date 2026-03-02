package com.example.ubercloneapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore
) : ViewModel() {
    var photoUrl   by mutableStateOf<String?>(null)
        private set
    var totalRides by mutableStateOf(0)
        private set
    var isUploading by mutableStateOf(false)
        private set

    val email: String
        get() = auth.currentUser?.email ?: "Sin email"

    init { loadProfile() }

    private fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            // Contar viajes del usuario
            val rides = db.collection("rides")
                .whereEqualTo("userId", uid)
                .get().await()
            totalRides = rides.size()

            // Obtener foto de perfil (si existe)
            try {
                val ref = storage.reference.child("profile_photos/$uid.jpg")
                // ↑ Ruta en Storage: profile_photos/{userId}.jpg
                // Cada usuario tiene su propia foto, identificada por UID.
                photoUrl = ref.downloadUrl.await().toString()
            } catch (_: Exception) {
                // No hay foto — se muestra un placeholder.
            }
        }
    }

    // ═══════════════════════════════════════════
    //  SUBIR FOTO DE PERFIL
    // ═══════════════════════════════════════════
    fun uploadPhoto(imageUri: Uri) {
        val uid = auth.currentUser?.uid ?: return
        isUploading = true

        viewModelScope.launch {
            try {
                val ref = storage.reference.child("profile_photos/$uid.jpg")

                // Subir el archivo
                ref.putFile(imageUri).await()
                // ↑ putFile() sube los bytes del Uri a Firebase Storage.
                // Es una tarea asíncrona — .await() espera a que termine.

                // Obtener la URL pública de descarga
                photoUrl = ref.downloadUrl.await().toString()
                // ↑ Firebase Storage genera una URL larga con un token.
                // Esa URL es pública y Coil la puede cargar.

            } catch (e: Exception) {
                Log.e("Profile", "Error al subir foto", e)
            } finally {
                isUploading = false
            }
        }
    }
}