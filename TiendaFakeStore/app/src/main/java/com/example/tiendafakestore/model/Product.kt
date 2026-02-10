package com.example.tiendafakestore.model

//datos que se pediran posteriormente a la api
// tienen que ser exactamanente igual.

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String //url de la imagen
)
