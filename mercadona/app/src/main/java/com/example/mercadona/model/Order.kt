package com.example.mercadona.model

data class OrderItem(
    val productId:   Int    = 0,
    val productName: String = "",
    val price:       Double = 0.0,
    val quantity:    Int    = 0
)

data class Order(
    val userId:  String          = "",   // UID del usuario (de Firebase Auth)
    val items:   List<OrderItem> = emptyList(),
    val total:   Double          = 0.0,
    val date:    String          = ""    // Fecha del pedido
)