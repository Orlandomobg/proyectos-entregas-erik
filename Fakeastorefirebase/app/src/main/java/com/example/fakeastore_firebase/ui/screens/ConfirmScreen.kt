package com.example.fakeastore_firebase.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fakeastore_firebase.viewmodel.StoreViewModel

@Composable
fun ConfirmScreen(
    viewModel:    StoreViewModel,
    onBackToHome: () -> Unit     // Volver al catálogo limpiando la pila
) {
    val cart   = viewModel.cartItems
    val result = viewModel.sendResult

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (result == null) {
            // ── ANTES DE ENVIAR: resumen del pedido ──
            Text("📋 Resumen del pedido",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(16.dp))

            cart.forEach { item ->
                Text("• ${item.product.title} × ${item.quantity}")
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "Total: ${"%.2f".format(viewModel.cartTotal)} €",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.sendCart() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar pedido (POST)")
            }
        } else {
            // ── DESPUÉS DE ENVIAR: resultado ──
            Text(result,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center)

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.clearCart()   // Vaciar el carrito
                    onBackToHome()          // Volver al catálogo
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al catálogo")
            }
        }
    }
}