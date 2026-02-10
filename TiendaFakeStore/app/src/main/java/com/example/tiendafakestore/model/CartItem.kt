package com.example.tiendafakestore.model

data class CartItem(
    val product: Product, //producto completo
    val quantity: Int = 1 // cantidad(por defecto)
)
