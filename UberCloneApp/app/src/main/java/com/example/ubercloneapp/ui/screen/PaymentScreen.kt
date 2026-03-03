package com.example.ubercloneapp.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Imports de Stripe ──
import com.stripe.android.PaymentConfiguration
// ↑ Configura la Publishable Key para todo el SDK.
import com.stripe.android.paymentsheet.PaymentSheet
// ↑ La clase principal: presenta la hoja de pago.
import com.stripe.android.paymentsheet.PaymentSheetResult
// ↑ Resultado: Completed, Canceled, o Failed.
import com.stripe.android.paymentsheet.rememberPaymentSheet
// ↑ Versión Compose de PaymentSheet. Recuerda la instancia
// entre recomposiciones (como rememberNavController).

import com.example.ubercloneapp.viewmodel.PaymentState
import com.example.ubercloneapp.viewmodel.PaymentViewModel

@Composable
fun PaymentScreen(
    paymentVm:   PaymentViewModel,
    ridePrice:   Double,          // Precio del viaje en euros
    rideSummary: String,          // "Mi ubicación → Destino"
    onPaymentOk: () -> Unit,     // Navegar tras pago exitoso
    onBack:      () -> Unit
) {
    val context = LocalContext.current
    val state = paymentVm.paymentState

    // ── Inicializar Stripe con la Publishable Key ──
    LaunchedEffect(Unit) {
        PaymentConfiguration.init(context, "pk_test_51T2XxMAhNkNhrbIgdN3AQ99ZhrRTSW3nO0qjERPZqDEjJbA8a2kWqRDWvqBJ3FBtk1y5HrfCETB6vZ25xauKjE8O00wnyHEO5v")
        // ↑ Configura el SDK de Stripe. Solo la clave PÚBLICA.
        // En producción, carga esto desde local.properties o BuildConfig.
    }

    // ── Crear PaymentSheet con callback de resultado ──
    val paymentSheet = rememberPaymentSheet { result ->
        // ↑ rememberPaymentSheet = versión Compose del PaymentSheet.
        // El callback se ejecuta cuando el usuario termina de pagar
        // (o cancela, o falla).
        when (result) {
            is PaymentSheetResult.Completed -> {
                paymentVm.onPaymentSuccess()
            }
            is PaymentSheetResult.Canceled -> {
                paymentVm.onPaymentCancelled()
            }
            is PaymentSheetResult.Failed -> {
                paymentVm.onPaymentFailed(result.error.message)
            }
        }
    }

    // ── Cuando tenemos el clientSecret, presentar PaymentSheet ──
    LaunchedEffect(state) {
        if (state is PaymentState.ReadyToPay) {
            paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret = state.clientSecret,
                // ↑ El secreto que nos dio nuestro servidor.
                configuration = PaymentSheet.Configuration(
                    merchantDisplayName = "UberClone",
                    // ↑ Nombre que el usuario ve en la hoja de pago.
                )
            )
        }
        if (state is PaymentState.Success) {
            onPaymentOk()
        }
    }

    // ── UI ──
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("💳", fontSize = 48.sp)
        Spacer(Modifier.height(16.dp))
        Text("Pagar viaje",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(12.dp))

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                Text(rideSummary)
                Text("Total: ${ridePrice}€",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall)
            }
        }

        Spacer(Modifier.height(24.dp))

        when (state) {
            is PaymentState.Loading -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text("Preparando pago...")
            }
            is PaymentState.Error -> {
                Text(state.msg,
                    color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(12.dp))
                Button(onClick = {
                    paymentVm.createPaymentIntent(ridePrice, rideSummary)
                }) { Text("Reintentar pago") }
            }
            is PaymentState.Success -> {
                Text("✅ ¡Pago completado!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary)
            }
            else -> {
                // Idle o ReadyToPay
                Button(
                    onClick = {
                        paymentVm.createPaymentIntent(ridePrice, rideSummary)
                        // ↑ Pide el clientSecret al servidor.
                        // Cuando llega, LaunchedEffect abre el PaymentSheet.
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("💳 Pagar ${ridePrice}€",
                        style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onBack) { Text("← Volver") }
    }
}