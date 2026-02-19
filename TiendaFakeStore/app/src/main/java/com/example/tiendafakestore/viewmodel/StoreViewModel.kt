package com.example.tiendafakestore.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendafakestore.model.CartItem
import com.example.tiendafakestore.model.CartProductRequest
import com.example.tiendafakestore.model.CartRequest
import com.example.tiendafakestore.model.Product
import com.example.tiendafakestore.network.RetrofitClient
import kotlinx.coroutines.launch
import java.time.LocalDate

//ESTADOS DE LA PANTALLA

sealed interface ProductsState{
    data object Loading : ProductsState
    data class Success(val products: List<Product>): ProductsState
    data class Error(val message: String): ProductsState
}

class StoreViewModel: ViewModel() {

    // estados de los productos que vienen de la api
    var productsState: ProductsState by mutableStateOf(ProductsState.Loading)
        private set

    var cartItems: List<CartItem> by mutableStateOf(emptyList())
        private set

    var sendResult: String? by mutableStateOf(null)
    private set



    init {
        // se ejecuta al crear el viewmodel(abrir la app)
        loadProducts()
    }

    private fun loadProducts() {
        productsState = ProductsState.Loading
        viewModelScope.launch{
            productsState = try {
                val products = RetrofitClient.apiService.getProducts()
                ProductsState.Success(products)
            } catch (e: Exception) {
                ProductsState.Error(e.message ?: "Error desconocido")
            }

        }
    }

    // GESTIONAR EL CARRITO

    // añadir prductos
    fun addToCart(product: Product) {
        val existing = cartItems.find { it.product.id == product.id }
        cartItems = if (existing != null) {
            cartItems.map{
                if(it.product.id == product.id) {
                    it.copy(quantity = it.quantity + 1)
                } else {
                    it
                }
            }
        } else {
            cartItems + CartItem(product)
            }
        }


    fun updateQuantity(productId: Int, newQty:Int){
        if(newQty < 1){
            removeFromCart(productId)
            return
        }

        cartItems = cartItems.map {
            if(it.product.id == productId){
                it.copy(quantity = newQty)
            } else {
                it
            }
        }
    }

    fun removeFromCart(productId: Int) {
        cartItems = cartItems.filter { it.product.id != productId }
    }

    val cartTotal: Double
        get() = cartItems.sumOf { it.product.price * it.quantity }

    //ENVIAR EL CARRITO POR POST

    fun sendCart() {
        if(cartItems.isEmpty()) return

        // convetimos nuestro cart item a lo que la api espera
        val request = CartRequest(
            userId = 1, // user ficticio
            date = LocalDate.now().toString(),
            products = cartItems.map {
                CartProductRequest(
                    productId = it.product.id,
                    quantity = it.quantity
                )
            }
        )

        viewModelScope.launch {
            sendResult = try {
                val response = RetrofitClient.apiService.createCart(request)
                "Pedido enviado (id: ${response["id"]})"
            } catch(e: Exception) {
                "Error: ${e.localizedMessage}"
            }
        }
    }
    fun doublequan(){
        cartItems = cartItems.map {it.copy(quantity = it.quantity * 2)}
    }

    fun clearResult() { sendResult = null }
}