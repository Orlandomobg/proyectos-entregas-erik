package com.example.tiendafakestore.network

import com.example.tiendafakestore.model.CartRequest
import com.example.tiendafakestore.model.Product
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    //traer productos
    @GET("products")
    suspend fun getProducts(): List<Product>
    // devuelve lista de prductos
    // se concatena con la base url

    @GET("category")
    suspend fun getCategory(): List<Product>
    // devuelve lista de prductos
    // se concatena con la base url


    //envia carrito
    @POST("carts")
    suspend fun createCart(@Body cart: CartRequest):Map<String,Any>
    // envia el body como json
    // la api devuelve mapa generico con id



    //actualizar el producto
    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Product
}
