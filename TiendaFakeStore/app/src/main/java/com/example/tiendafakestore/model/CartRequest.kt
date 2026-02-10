package com.example.tiendafakestore.model

data class CartRequest(
    val userId: Int, //usuario ficticio
    val date: String, //fecha de solicitud
    val products: List<CartProductRequest> //lista de productos
)

data class CartProductRequest(
    val productId: Int,
    val quantity: Int
)