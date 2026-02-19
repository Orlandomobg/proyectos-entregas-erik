package com.example.mercadona.network

import com.example.mercadona.model.CartRequest
import com.example.mercadona.model.Product
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {


    // ── TRAER productos ──
    @GET("products")
    suspend fun getProducts(): List<Product>
    // GET a /products → devuelve lista de Product

    // ── ENVIAR carrito ──
    @POST("carts")
    suspend fun createCart(@Body cart: CartRequest): Map<String, Any>
    // POST a /carts → envia el body como JSON
    // como limitar productos @Body() cart: CartRequest

    // cATEGORIAS
    @GET("products")
    suspend fun getCategories(): List<Product>
    // GET a /products/{id)

}