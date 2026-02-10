package com.example.tiendafakestore.network

import com.example.tiendafakestore.model.CartRequest
import com.example.tiendafakestore.model.Product
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    //traer productos
    @GET("products")
    suspend fun getProducts(): List<Product>
    // devuelve lista de prductos
    // se concatena con la base url



    //envia carrito
    @POST("carts")
    suspend fun createCart(@Body cart: CartRequest):Map<String,Any>
    // envia el body como json
    // la api devuelve mapa generico con id
}
