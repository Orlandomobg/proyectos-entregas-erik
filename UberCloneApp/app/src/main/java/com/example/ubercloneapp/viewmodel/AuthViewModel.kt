package com.example.ubercloneapp.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ── Credential Manager (Google Sign-In moderno) ──
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

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
    //  REGISTRAR usuario nuevo (email/contraseña)
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
    //  LOGIN con usuario existente (email/contraseña)
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
    //  LOGIN CON GOOGLE  ← NUEVO
    // ═══════════════════════════════════════════
    // Este método usa Credential Manager (la API moderna de Android)
    // para mostrar el selector de cuentas de Google, obtener un
    // ID Token, y con ese token autenticarse en Firebase.
    //
    // Necesita un Context porque Credential Manager lo usa para
    // lanzar la UI del sistema (el bottom sheet con las cuentas).
    //
    // WEB_CLIENT_ID viene del google-services.json → oauth_client → client_type: 3

    companion object {
        private const val WEB_CLIENT_ID =
            "986849892680-4g770kl8805u4rmdj44b1eiua1hth23m.apps.googleusercontent.com"
        // OJO (ESTO SIRVE PARA QUE SIGN IN DE GOOGLE FUNCIONE)
        // ↑ SUSTITUYE esto por tu Web Client ID real.
        // Lo encuentras en google-services.json → oauth_client → client_type: 3
        // o en Firebase Console → Authentication → Google → Web Client ID
    }

    fun signInWithGoogle(context: Context) {
        authState = AuthState.Loading

        viewModelScope.launch {
            try {
                // ① Crear el Credential Manager
                // Es la API unificada de Android para credenciales.
                // Reemplaza al antiguo GoogleSignInClient (deprecado en 2023).
                val credentialManager = CredentialManager.create(context)

                // ② Configurar la petición de Google ID
                // Le decimos: "quiero un token de Google, usa este Client ID
                // como audiencia, y filtra por cuentas autorizadas".
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    // ↑ false = muestra TODAS las cuentas de Google del dispositivo.
                    // true = solo muestra cuentas que ya hayan iniciado sesión
                    // en tu app antes (útil para "iniciar sesión rápido").
                    .setServerClientId(WEB_CLIENT_ID)
                    // ↑ El Web Client ID. Google lo usa como "audiencia" del token.
                    // Firebase lo verifica para asegurarse de que el token
                    // fue emitido para TU proyecto y no para otro.
                    .build()

                // ③ Construir el request
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    // ↑ Puedes añadir más opciones aquí (ej: passkeys, contraseñas
                    // guardadas) pero para nuestro caso solo queremos Google.
                    .build()

                // ④ Lanzar el selector de cuentas
                // Esto muestra el bottom sheet del sistema con las cuentas
                // de Google disponibles en el dispositivo.
                // El usuario elige una → Google devuelve una Credential.
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                // ⑤ Extraer el Google ID Token de la credencial
                // La respuesta es genérica (puede ser contraseña, passkey, etc.)
                // así que verificamos que sea un CustomCredential de tipo Google.
                val credential = result.credential

                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    // ⑥ Parsear el token
                    val googleIdToken = GoogleIdTokenCredential
                        .createFrom(credential.data)
                        .idToken
                    // ↑ idToken es un JWT (JSON Web Token) firmado por Google.
                    // Contiene: email, nombre, foto, ID único del usuario.
                    // Firebase lo verifica con la clave pública de Google.

                    // ⑦ Crear credencial de Firebase con el token de Google
                    val firebaseCredential = GoogleAuthProvider
                        .getCredential(googleIdToken, null)
                    // ↑ GoogleAuthProvider convierte el ID token de Google
                    // en una AuthCredential que Firebase entiende.
                    // El segundo parámetro (accessToken) es null porque
                    // solo necesitamos el ID token para autenticación.

                    // ⑧ Autenticarse en Firebase con esa credencial
                    auth.signInWithCredential(firebaseCredential).await()
                    // ↑ Firebase verifica el token con Google, y si es válido:
                    // - Si el usuario NO existe → lo CREA automáticamente
                    // - Si el usuario YA existe → simplemente inicia sesión
                    // Por eso Google Sign-In sirve como login Y como registro.

                    authState = AuthState.Authenticated

                } else {
                    authState = AuthState.Error("Tipo de credencial inesperado")
                }

            } catch (e: Exception) {
                authState = AuthState.Error(
                    e.localizedMessage ?: "Error al iniciar con Google"
                )
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