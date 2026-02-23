package com.example.uber

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    // Datos del formulario
    var phoneNumber by mutableStateOf("")
        private set
    var countryCode by mutableStateOf("+34")
    var verificationId by mutableStateOf("") // ID que nos da Firebase para el SMS


    // Validar con libphonenumber
    fun isPhoneValid(): Boolean {
        return try {
            val fullNumber = "$countryCode$phoneNumber"
            val numberProto = phoneUtil.parse(fullNumber, null)
            phoneUtil.isValidNumber(numberProto)
        } catch (e: Exception) {
            false
        }
    }

    // PASO 1: Enviar SMS
    fun sendVerificationCode(activity: android.app.Activity) {
        if (!isPhoneValid()) {
            authState = AuthState.Error("Número de teléfono no válido")
            return
        }

        authState = AuthState.Loading
        val fullNumber = "$countryCode$phoneNumber"

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

    fun logout() {
        auth.signOut()
        authState = AuthState.Idle
    }
}

fun onPhoneChanged(newValue: String) {
    phoneNumber = newValue
}