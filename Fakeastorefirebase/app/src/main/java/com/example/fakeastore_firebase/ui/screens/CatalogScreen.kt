package com.example.fakeastore_firebase.ui.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fakeastore_firebase.model.Product
import com.example.fakeastore_firebase.viewmodel.ProductsState
import com.example.fakeastore_firebase.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: StoreViewModel,
    onProductClick: (Int) -> Unit,   // Recibe el productId
    onCartClick:    () -> Unit,       // Ir al carrito
    onLogout:       () -> Unit = {}   // Acción de cerrar sesión
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏪 Catálogo") },
                actions = {
                    // Botón del carrito
                    TextButton(onClick = onCartClick) {
                        Text("🛒 ${viewModel.cartCount}")
                    }
                    // Botón de Logout
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        }
    ) { padding ->

        when (val state = viewModel.productsState) {

            is ProductsState.Loading ->
                Box(Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

            is ProductsState.Error ->
                Box(Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center) {
                    Text("❌ ${state.error}")
                }

            is ProductsState.Success ->
                LazyColumn(
                    Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.products, key = { it.id }) { product ->
                        CatalogCard(
                            product = product,
                            // Al tocar la tarjeta → navegar al detalle
                            onClick = { onProductClick(product.id) },
                            // Botón rápido "Añadir" sin ir al detalle
                            onAdd   = { viewModel.addToCart(product) }
                        )
                    }
                }
        }
    }
}

@Composable
fun CatalogCard(
    product: Product,
    onClick: () -> Unit,   // Tocar la tarjeta → detalle
    onAdd:   () -> Unit    // Botón "Añadir" → carrito
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }  // Toda la tarjeta es tocable
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(product.title, fontWeight = FontWeight.Bold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${product.price} €",
                    color = MaterialTheme.colorScheme.primary)
            }
            FilledTonalButton(onClick = onAdd) { Text("+") }
        }
    }
}