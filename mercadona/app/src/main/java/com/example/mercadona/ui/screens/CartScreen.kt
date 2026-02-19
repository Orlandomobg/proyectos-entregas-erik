package com.example.mercadona.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mercadona.model.CartItem
import com.example.mercadona.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: StoreViewModel,
    onBack:    () -> Unit,     // Volver atrás
    onConfirm: () -> Unit      // Ir a confirmación
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🛒 Tu carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                    }
                }
            )
        }
    ) { padding ->

        val cart = viewModel.cartItems

        if (cart.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center) {
                Text("El carrito está vacío",
                    color = MaterialTheme.colorScheme.outline)
            }
        } else {
            Column(Modifier.fillMaxSize().padding(padding)) {

                // Lista de artículos (scrollable)
                LazyColumn(
                    Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cart, key = { it.product.id }) { item ->
                        CartItemRow(
                            item     = item,
                            onPlus   = { viewModel.updateQuantity(item.product.id, item.quantity + 1) },
                            onMinus  = { viewModel.updateQuantity(item.product.id, item.quantity - 1) },
                            onRemove = { viewModel.removeFromCart(item.product.id) }
                        )
                    }
                }

                // Barra inferior fija con total y botón
                HorizontalDivider()
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Total: ${"%.2f".format(viewModel.cartTotal)} €",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Button(onClick = onConfirm) {
                        Text("Confirmar pedido →")
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item:     CartItem,
    onPlus:   () -> Unit,
    onMinus:  () -> Unit,
    onRemove: () -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(item.product.title, fontWeight = FontWeight.Bold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${item.product.price} € × ${item.quantity}",
                    color = MaterialTheme.colorScheme.outline)
            }
            TextButton(onClick = onMinus)  { Text("−") }
            Text("${item.quantity}", fontWeight = FontWeight.Bold)
            TextButton(onClick = onPlus)   { Text("+") }
            TextButton(onClick = onRemove) { Text("🗑") }
        }
    }
}