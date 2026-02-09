package com.example.practica_teoria

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp




data class ProductoEnCarrito(
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val onCantidadCambia: (Int) -> Unit
)


@Composable
fun CarritoDeCompras() {

    var cantidadCamiseta by remember { mutableStateOf(1) }
    var cantidadPantalon by remember { mutableStateOf(1) }

    val precioCamiseta = 29.99
    val precioPantalon = 49.99



    Column(modifier = Modifier.fillMaxWidth()
        .padding(16.dp)) {

        Text("Tu carrito", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        ProductoEnCarrito(
            nombre = "Camiseta Azul",
            cantidad = cantidadCamiseta,
            precio = precioCamiseta,
            onCantidadCambia = { nuevaCantidad ->
                if (nuevaCantidad in 1..10) {
                    cantidadCamiseta = nuevaCantidad
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProductoEnCarrito(
            nombre = "Pantalón Negro",
            cantidad = cantidadPantalon,
            precio = precioPantalon,
            onCantidadCambia = { nuevaCantidad ->
                if (nuevaCantidad in 1..10) {
                    cantidadPantalon = nuevaCantidad
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        val total =
            (cantidadCamiseta * precioCamiseta) +
                    (cantidadPantalon * precioPantalon)

        Text(
            text = "Total: $${String.format("%.2f", total)}",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
