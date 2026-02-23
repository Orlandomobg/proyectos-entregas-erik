package com.example.uber_carlos.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

sealed interface AuthState{
    data object Idle: AuthState // E.inicial
    data object Loading: AuthState // progress bar
    data class CodeSent(val verificationId: String): AuthState // sms
    data object Authenticated: AuthState // login exito
    data class Error(val message: String): AuthState // error
}

class AuthViewModel : ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    //ui stado
    var authState: AuthState by mutableStateOf(AuthState.Idle)
        private set

    val currentUser: FirebaseUser? get() = auth.currentUser // propiedad calculad
    val isLoggedIn: Boolean get() = currentUser != null


    //numero sms
    var phoneNumber by mutableStateOf("")
        private set

    fun onPhoneChanged(newValue: String) {
        phoneNumber = newValue
    }

    fun sendCode(activity: Activity) {
        if (phoneNumber.length < 7) {
            authState= AuthState.Error("Invalid phone number")
            return
        }
    /* La [Activity] actual que Firebase requiere para gestionar
    las devoluciones de llamada de verificación.*/
        authState = AuthState.Loading
        // override: (Sobrescribir/Anular)
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential)
                }
                    /*Solo en algunos teléfonos Android donde Google puede verificar que el número
                     es tuyo automáticamente (a veces interceptando el SMS al vuelo o por servicios de Google Play)
                      */
                override fun onVerificationFailed(e: FirebaseException) {
                    authState = AuthState.Error(e.message ?: "Verification failed")
                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    authState = AuthState.CodeSent(id)
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    fun verifyOtp(otpCode: String) {
        val currentState = authState
        if (currentState is AuthState.CodeSent) {
            val credential = PhoneAuthProvider.getCredential(currentState.verificationId, otpCode)
            signInWithCredential(credential)
        }
    }
    private fun signInWithCredential(credential: PhoneAuthCredential) {
        authState = AuthState.Loading
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                authState = AuthState.Authenticated
            } else {
                authState = AuthState.Error("Invalid code")
            }
        }
    }
    fun logout() {
        auth.signOut()
        authState = AuthState.Idle
    }


}
/* Gestiona el estado: Controla si estamos en reposo, cargando, con código
enviado, con error o autenticados.

Valida la entrada: Se asegura de que el número sea válido antes de enviarlo.

Se comunica con Firebase: Configura y lanza la petición del SMS.

Verifica la identidad: Comprueba que el código que pone el usuario
sea el correcto para iniciar la sesión.
*/
