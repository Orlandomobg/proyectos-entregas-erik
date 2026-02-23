package com.example.ubercloneapp.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ═══════════════════════════════════════════
//  ESTADOS DE LA AUTENTICACIÓN
// ═══════════════════════════════════════════
// sealed interface = un tipo que SOLO puede ser uno de estos valores.
// Es como un enum pero más potente (puede llevar datos, como Error).
// Compose observa este estado y repinta la UI según corresponda.

sealed interface AuthState {
    data object Idle          : AuthState  // No ha intentado nada todavía
    data object Loading       : AuthState  // Esperando respuesta de Firebase
    data object Authenticated : AuthState  // Login/registro correcto
    data class  Error(val msg: String) : AuthState  // Algo falló
}

class AuthViewModel : ViewModel() {

    // ── Instancia de Firebase Auth (singleton) ──
    // FirebaseAuth.getInstance() siempre devuelve la MISMA instancia.
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // ── Estado observable por las pantallas ──
    var authState: AuthState by mutableStateOf(AuthState.Idle)
        private set
    // ↑ "private set" = solo este ViewModel puede cambiar el valor.
    // Las pantallas pueden LEER pero no ESCRIBIR.

    // ── ¿Hay usuario logueado ahora mismo? ──
    val currentUser: FirebaseUser?
        get() = auth.currentUser
    // ↑ Propiedad calculada. Cada vez que la lees, pregunta a Firebase.
    // Firebase persiste la sesión en disco → al reabrir la app,
    // currentUser no es null si el usuario ya se logueó antes.

    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    // ═══════════════════════════════════════════
    //  REGISTRAR usuario nuevo
    // ═══════════════════════════════════════════
    fun register(email: String, password: String) {
        authState = AuthState.Loading
        viewModelScope.launch {
            authState = try {
                auth.createUserWithEmailAndPassword(email, password).await()
                // ↑ Crea el usuario en Firebase. .await() espera sin congelar la UI.
                AuthState.Authenticated
            } catch (e: Exception) {
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
        auth.signOut()
        authState = AuthState.Idle
    }

    fun clearError() { authState = AuthState.Idle }
}