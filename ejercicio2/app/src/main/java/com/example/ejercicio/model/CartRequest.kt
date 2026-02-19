package com.example.ejercicio.model

data class CartRequest(
    val userId: Int,
    val date: String,
    val products: List<CartProductRequest>
)

data class CartProductRequest(
    val productId: Int,
    val quantity: Int
)
