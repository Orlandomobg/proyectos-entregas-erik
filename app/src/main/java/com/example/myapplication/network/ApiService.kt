package com.example.myapplication.network

import com.example.myapplication.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts") //de como se va a llamar en la url de la api
    suspend fun getPosts(): List<Post>
    //"suspend" = no bloquea la app mientras espera
    // devuelve una lista de post ya convertida desde JSON
}