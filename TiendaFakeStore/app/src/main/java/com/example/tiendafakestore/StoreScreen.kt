package com.example.tiendafakestore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tiendafakestore.model.CartItem
import com.example.tiendafakestore.model.Product
import com.example.tiendafakestore.viewmodel.ProductsState
import com.example.tiendafakestore.viewmodel.StoreViewModel

// ═══════════════════════════════════════════
//  PANTALLA PRINCIPAL
// ═══════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(vm: StoreViewModel = viewModel()) {

    // Leemos el estado. Compose observa estas variables.
    val productsState = vm.productsState
    val cart          = vm.cartItems
    val sendResult    = vm.sendResult

    // Snackbar para mostrar el resultado del POST
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(sendResult) {
        sendResult?.let {
            snackbar.showSnackbar(it)
            vm.clearResult()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("🛒 FakeStore") }) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->

        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {

            // ── SECCIÓN 1: CATÁLOGO ──
            Text("Productos", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            when (productsState) {
                is ProductsState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                is ProductsState.Error ->
                    Text("❌ ${productsState.message}",
                        color = MaterialTheme.colorScheme.error)
                is ProductsState.Success -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f), // Ocupa el espacio sobrante
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(productsState.products, key = { it.id }) { product ->
                            ProductCard(
                                product = product,
                                onAdd    = { vm.addToCart(product) }
                            )
                        }
                    }
                }
            }

            // ── SECCIÓN 2: CARRITO ──
            if (cart.isNotEmpty()) {
                HorizontalDivider(Modifier.padding(vertical = 12.dp))
                Text("Tu carrito", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                cart.forEach { item ->
                    CartRow(
                        item       = item,
                        onPlus     = { vm.updateQuantity(item.product.id, item.quantity + 1) },
                        onMinus    = { vm.updateQuantity(item.product.id, item.quantity - 1) },
                        onRemove   = { vm.removeFromCart(item.product.id) }
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    "Total: ${"%.2f".format(vm.cartTotal)} €",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { vm.sendCart() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar Pedido (POST)")
                }
            }
        }
    }
}

// ═══════════════════════════════════════════
//  TARJETA DE PRODUCTO (catálogo)
// ═══════════════════════════════════════════

@Composable
fun ProductCard(product: Product, onAdd: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {

            // Imagen del producto (cargada desde URL con Coil)
            AsyncImage(
                model       = product.image,
                contentDescription = product.title,
                modifier    = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(product.title, fontWeight = FontWeight.Bold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${product.price} €",
                    color = MaterialTheme.colorScheme.primary)
            }

            FilledTonalButton(onClick = onAdd) {
                Text("Añadir")
            }
        }
    }
}

// ═══════════════════════════════════════════
//  FILA DEL CARRITO
// ═══════════════════════════════════════════

@Composable
fun CartRow(
    item:     CartItem,
    onPlus:   () -> Unit,
    onMinus:  () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nombre (ocupa el espacio disponible)
        Text(
            item.product.title,
            Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Controles de cantidad: [−] cantidad [+]
        TextButton(onClick = onMinus) { Text("−") }
        Text("${item.quantity}", fontWeight = FontWeight.Bold)
        TextButton(onClick = onPlus)  { Text("+") }

        // Botón eliminar
        TextButton(onClick = onRemove) { Text("🗑") }
    }
}