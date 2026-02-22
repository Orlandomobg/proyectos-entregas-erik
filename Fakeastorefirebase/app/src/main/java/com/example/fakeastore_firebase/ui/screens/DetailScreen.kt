package com.example.fakeastore_firebase.ui.screens

import coil.compose.AsyncImage
import com.example.fakeastore_firebase.viewmodel.StoreViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    productId: Int,              // ← Dato recibido de la pantalla anterior
    viewModel: StoreViewModel,    // ← Estado global compartido
    onBack:    () -> Unit,       // ← Volver atrás
    onGoToCart: () -> Unit       // ← Ir al carrito
) {
    // Buscamos el producto por ID en los datos ya descargados
    val product = viewModel.getProductById(productId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                // ── FLECHA ATRÁS ──
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                    }
                }
            )
        }
    ) { padding ->

        if (product == null) {
            // Si no encontramos el producto (no debería pasar)
            Box(Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center) {
                Text("Producto no encontrado")
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()) // Scroll manual
                    .padding(16.dp)
            ) {
                // Imagen grande
                AsyncImage(
                    model = product.image,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.height(16.dp))
                Text(product.title, style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("${product.price} €",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(4.dp))
                Text(product.category,
                    color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(16.dp))
                Text(product.description)

                Spacer(Modifier.height(24.dp))

                // ── BOTONES DE ACCIÓN ──
                Button(
                    onClick = {
                        viewModel.addToCart(product)  // Añade al estado global
                        onGoToCart()                   // Navega al carrito
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir y ver carrito")
                }

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.addToCart(product)  // Añade al estado global
                        onBack()                       // Vuelve al catálogo
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir y seguir comprando")
                }
            }
        }
    }
}