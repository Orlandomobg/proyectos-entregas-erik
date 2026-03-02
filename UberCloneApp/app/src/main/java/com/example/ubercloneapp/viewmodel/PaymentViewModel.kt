package com.example.ubercloneapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubercloneapp.network.PaymentRequest
import com.example.ubercloneapp.network.StripeClient
import kotlinx.coroutines.launch

// ═══════════════════════════════════════════
//  ESTADOS DEL PAGO
// ═══════════════════════════════════════════

sealed interface PaymentState {
    data object Idle    : PaymentState  // No ha iniciado pago
    data object Loading : PaymentState  // Creando PaymentIntent/Session

    data class ReadyToPay(
        val clientSecret: String
    ) : PaymentState
    // ↑ Opción A: tenemos el clientSecret, listo para PaymentSheet

    data class CheckoutUrl(
        val url: String
    ) : PaymentState
    // ↑ Opción B: tenemos la URL de Stripe Checkout

    data object Success : PaymentState  // Pago exitoso
    data class  Error(val msg: String) : PaymentState
}

class PaymentViewModel : ViewModel() {

    var paymentState: PaymentState by mutableStateOf(PaymentState.Idle)
        private set

    // ═══════════════════════════════════════════
    //  OPCIÓN A: Obtener clientSecret para PaymentSheet
    // ═══════════════════════════════════════════
    fun createPaymentIntent(amountEuros: Double, rideSummary: String) {
        android.util.Log.d("Payment", "Amount euros: $amountEuros")
        android.util.Log.d("Payment", "Amount cents: ${(amountEuros * 100).toInt()}")
        paymentState = PaymentState.Loading

        // Convertir euros a céntimos (Stripe trabaja en céntimos)
        val amountCents = (amountEuros * 100).toInt()

        viewModelScope.launch {
            paymentState = try {
                val response = StripeClient.api.createPaymentIntent(
                    PaymentRequest(
                        amount = amountCents,
                        rideSummary = rideSummary
                    )
                )
                PaymentState.ReadyToPay(response.clientSecret)
            } catch (e: Exception) {
                PaymentState.Error(e.localizedMessage ?: "Error al crear pago")
            }
        }
    }
    // Resultado del PaymentSheet (lo llama la pantalla)
    fun onPaymentSuccess() { paymentState = PaymentState.Success }
    fun onPaymentFailed(msg: String?) {
        paymentState = PaymentState.Error(msg ?: "Pago fallido")
    }
    fun onPaymentCancelled() {
        paymentState = PaymentState.Idle
    }
    fun resetPayment() { paymentState = PaymentState.Idle }
}
