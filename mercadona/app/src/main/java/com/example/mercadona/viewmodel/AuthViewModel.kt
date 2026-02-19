package com.example.mercadona.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed interface AuthState {
    data object Idle        : AuthState  // No ha intentado nada todavía
    data object Loading     : AuthState  // Esperando respuesta de Firebase
    data object Authenticated : AuthState  // Login correcto
    data class  Error(val msg: String) : AuthState  // Algo falló
}

class AuthViewModel : ViewModel() {

    // ── Instancia de Firebase Auth (singleton) ──
    // FirebaseAuth.getInstance() devuelve siempre la MISMA instancia.
    // "private" = solo este ViewModel la usa directamente.
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // ── Estado observable por las pantallas ──
    var authState: AuthState by mutableStateOf(AuthState.Idle)
        private set  // Solo el ViewModel puede cambiarlo

    // ── ¿Hay usuario logueado ahora mismo? ──
    // currentUser es null si no hay sesión, o un FirebaseUser si la hay.
    // Esto se comprueba al arrancar la app.
    val currentUser: FirebaseUser?
        get() = auth.currentUser
    // Propiedad calculada: cada vez que la lees, pregunta a Firebase.
    // Si el usuario cerró sesión, devuelve null automáticamente.

    // ── ¿Está logueado? (atajo booleano) ──
    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    // ═══════════════════════════════════════════
    //  REGISTRAR usuario nuevo
    // ═══════════════════════════════════════════

    fun register(email: String, password: String) {
        authState = AuthState.Loading

        viewModelScope.launch {
            // launch = ejecutar esto en segundo plano
            authState = try {
                // createUserWithEmailAndPassword devuelve un Task.
                // .await() lo convierte en suspend → espera sin congelar la UI.
                auth.createUserWithEmailAndPassword(email, password).await()
                AuthState.Authenticated
            } catch (e: Exception) {
                // Errores comunes: email ya existe, contraseña débil, sin internet.
                AuthState.Error(e.localizedMessage ?: "Error al registrar")
            }
        }
    }

    // ═══════════════════════════════════════════
    //  LOGIN con usuario existente
    // ═══════════════════════════════════════════

    fun login(email: String, password: String) {
        authState = AuthState.Loading

        viewModelScope.launch {
            authState = try {
                auth.signInWithEmailAndPassword(email, password).await()
                AuthState.Authenticated
            } catch (e: Exception) {
                AuthState.Error(e.localizedMessage ?: "Error al iniciar sesión")
            }
        }
    }

    // ═══════════════════════════════════════════
    //  LOGOUT
    // ═══════════════════════════════════════════

    fun logout() {
        auth.signOut()          // Firebase borra la sesión local
        authState = AuthState.Idle  // Volvemos al estado inicial
    }

    // Limpiar error para permitir reintento
    fun clearError() { authState = AuthState.Idle }
}