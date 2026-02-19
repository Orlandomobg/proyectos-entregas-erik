package com.example.mercadona.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mercadona.model.CartItem
import com.example.mercadona.model.CartProductRequest
import com.example.mercadona.model.CartRequest
import com.example.mercadona.model.Product
import com.example.mercadona.network.RetrofitClient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.LocalDate

// estado de la carga de productos

sealed interface ProductsState{
    data object Loading : ProductsState
    data class Success(val products: List<Product>): ProductsState
    data class Error(val error: String): ProductsState
}

class StoreViewModel: ViewModel() {

    // Estado Global- visible para todas las pantallas


    //Productos de la API
    var productsState: ProductsState by mutableStateOf(ProductsState.Loading)
        private set

    //Carrito
    var cartItems: List<CartItem> by mutableStateOf((emptyList()))
        private set

    //Resultado del envio
    var sendResult: String? by mutableStateOf(null)
        private set

    // Cargar Productos
    init { loadProducts() }

    private fun loadProducts() {
        productsState = ProductsState.Loading
        viewModelScope.launch {
            productsState = try {
                ProductsState.Success(RetrofitClient.apiService.getProducts())
            } catch (e: Exception) {
                ProductsState.Error(e.localizedMessage ?: "Error")
            }
        }
    }

    fun getProductById(id: Int): Product? {
        // Buscamos en la lista que ya tenemos descargada
        val state = productsState
        return if (state is ProductsState.Success) {
            state.products.find { it.id == id }
        } else null
    }

    //Buscar Producto por id
    fun addToCart(product: Product) {
        val existing = cartItems.find { it.product.id == product.id }
        cartItems = if (existing != null) {
            cartItems.map {
                if (it.product.id == product.id)
                    it.copy(quantity = it.quantity + 1)
                else it
            }
        } else {
            cartItems + CartItem(product)
        }
    }
    fun updateQuantity(productId: Int, newQty: Int) {
        if (newQty < 1) { removeFromCart(productId); return }
        cartItems = cartItems.map {
            if (it.product.id == productId) it.copy(quantity = newQty) else it
        }
    }

    fun removeFromCart(productId: Int) {
        cartItems = cartItems.filter { it.product.id != productId }
    }
    // it = cada item de la lista
    val cartTotal: Double
        get() = cartItems.sumOf { it.product.price * it.quantity }

    val cartCount: Int
        get() = cartItems.sumOf { it.quantity }

    fun sendCart() {
        if (cartItems.isEmpty()) return
        val request = CartRequest(
            userId = 1,
            date = LocalDate.now().toString(),
            products = cartItems.map {
                CartProductRequest(it.product.id, it.quantity)
            }
        )
        viewModelScope.launch {
            sendResult = try {
                val resp = RetrofitClient.apiService.createCart(request)
                "✅ Pedido enviado (id: ${resp["id"]})"
            } catch (e: Exception) {
                "❌ Error: ${e.localizedMessage}"
            }
        }
    }

    fun saveOrderToFirestore() {
        if (cartItems.isEmpty()) return
        
        val db = FirebaseFirestore.getInstance()
        val order = hashMapOf(
            "date" to LocalDate.now().toString(),
            "total" to cartTotal,
            "items" to cartItems.map { 
                hashMapOf(
                    "id" to it.product.id,
                    "name" to it.product.title,
                    "price" to it.product.price,
                    "quantity" to it.quantity
                )
            }
        )

        sendResult = "⏳ Enviando a Firestore..."
        
        db.collection("orders")
            .add(order)
            .addOnSuccessListener { documentReference ->
                sendResult = "✅ Pedido guardado en Firestore (ID: ${documentReference.id})"
                clearCart()
            }
            .addOnFailureListener { e ->
                sendResult = "❌ Error en Firestore: ${e.localizedMessage}"
            }
    }

    fun clearResult() { sendResult = null }
    fun clearCart()   { cartItems = emptyList(); sendResult = null }

}
