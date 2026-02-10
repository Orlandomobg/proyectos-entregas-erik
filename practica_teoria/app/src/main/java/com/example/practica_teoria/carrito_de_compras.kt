package com.example.practica_teoria

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp




/*data class ProductoEnCarrito(
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val onCantidadCambia: (Int) -> Unit
)*/


@Composable
fun CarritoDeCompras() {

    var cantidadCamiseta by remember { mutableStateOf(1) }
    var cantidadPantalon by remember { mutableStateOf(1) }

    val precioCamiseta = 29.99
    val precioPantalon = 49.99



    Column(modifier = Modifier.fillMaxWidth()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
) {


        Text("Tu carrito", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding( top =  20.dp))



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

        Spacer(modifier = Modifier.height(2.dp))

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
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable

fun ProductoEnCarrito (
    nombre: String,
    precio: Double,
    cantidad: Int,
    onCantidadCambia: (Int) -> Unit
) {
    Card(modifier = Modifier.padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black, contentColor = Color.White)){
        Row(modifier = Modifier.padding (16.dp),
            verticalAlignment = Alignment.CenterVertically,
            ){
                Column(modifier = Modifier.weight(1f)){
                    Text(nombre, fontWeight = FontWeight.Bold)
                    Text("$ ${precio} c/u")
                }

                Row(verticalAlignment = Alignment.CenterVertically,
                    ){
                    IconButton(
                        onClick = {onCantidadCambia(cantidad - 1)},
                        enabled = cantidad > 1
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Menos")
                    }

                    Text(
                        text = "$cantidad",
                        modifier = Modifier.width(40.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )

                    IconButton(
                        onClick = {onCantidadCambia(cantidad + 1)})
                    {
                        Icon(Icons.Default.Add, contentDescription = "Mas")
                    }
                }

                Text(
                    text = "$${String.format("%.2f", precio * cantidad)}",
                    modifier = Modifier.width(80.dp),
                    textAlign = TextAlign.End
                    )



    }
    }
}
