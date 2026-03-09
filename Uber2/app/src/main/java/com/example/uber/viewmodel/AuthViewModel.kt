package com.example.uber.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data object CodeSent : AuthState // Nuevo: El SMS ya se envió, toca pedir el OTP
    data object Authenticated : AuthState
    data class Error(val msg: String) : AuthState
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val phoneUtil = PhoneNumberUtil.getInstance()

    // Estado de la UI
    var authState: AuthState by mutableStateOf(AuthState.Idle)
        private set

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isLoggedIn: Boolean
        get() = auth.currentUser != null
    // Datos del formulario
    var phoneNumber by mutableStateOf("")
    var verificationId by mutableStateOf("") // ID que nos da Firebase para el SMS


    // Validar con libphonenumber
    fun isPhoneValid(): Boolean {
        return try {
            val fullNumber = phoneNumber
            val numberProto = phoneUtil.parse(fullNumber, "ES")
            val isValid = phoneUtil.isValidNumber(numberProto)
            println("¿Es válido? $isValid")
            isValid
        } catch (e: Exception) {
            println("Error validando número: ${e.message}")
            false
        }
    }

    // PASO 1: Enviar SMS
    fun sendVerificationCode(activity: Activity) {
        Log.d("AUTH", "Número: $phoneNumber")
        Log.d("AUTH", "Válido: ${isPhoneValid()}")
        if (!isPhoneValid()) {
            authState = AuthState.Error("Número de teléfono no válido")
            return
        }

        authState = AuthState.Loading
        val fullNumber = phoneNumber

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(fullNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // En algunos casos (autoverificación), Firebase loguea solo
                    signInWithPhoneAuthCredential(credential)
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    authState = AuthState.Error(e.localizedMessage ?: "Error en Firebase")
                }
                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationId = id
                    authState = AuthState.CodeSent // Cambiamos el estado para navegar al OTP
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    // PASO 2: Verificar el código que el usuario mete a mano
    fun verifyOtp(otpCode: String) {
        authState = AuthState.Loading
        val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            try {
                auth.signInWithCredential(credential).await()
                authState = AuthState.Authenticated
            } catch (e: Exception) {
                authState = AuthState.Error(e.localizedMessage ?: "Código OTP incorrecto")
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
    //  REGISTRAR usuario nuevo (email/contraseña)
    // ═══════════════════════════════════════════
    fun register(email: String, password: String) {
        authState = com.example.uber.viewmodel.AuthState.Loading
        viewModelScope.launch {
            authState = try {
                auth.createUserWithEmailAndPassword(email, password).await()
                // ↑ Crea el usuario en Firebase. .await() espera sin congelar la UI.
                com.example.uber.viewmodel.AuthState.Authenticated
            } catch (e: Exception) {
                com.example.uber.viewmodel.AuthState.Error(e.localizedMessage ?: "Error al registrar")
            }
        }
    }
    // ═══════════════════════════════════════════
    //  LOGIN CON GOOGLE  ← NUEVO
    // ═══════════════════════════════════════════
    companion object {
        private const val WEB_CLIENT_ID =
            "372788719910-pntuhvokftcvr7lhc79npn7dgpqngmrp.apps.googleusercontent.com"
    }

    fun signInWithGoogle(context: Context) {
        authState = AuthState.Loading

        viewModelScope.launch {
            try {

                val credentialManager = CredentialManager.create(context)

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(WEB_CLIENT_ID)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                val credential = result.credential

                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdToken = GoogleIdTokenCredential
                        .createFrom(credential.data)
                        .idToken

                    val firebaseCredential = GoogleAuthProvider
                        .getCredential(googleIdToken, null)

                    auth.signInWithCredential(firebaseCredential).await()

                    authState =AuthState.Authenticated

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


    fun logout() {
        auth.signOut()
        authState = AuthState.Idle
    }
    fun onPhoneChanged(newValue: String) {
        phoneNumber = newValue
    }

    fun resetState() {
        authState = AuthState.Idle
        verificationId = ""
    }
}
