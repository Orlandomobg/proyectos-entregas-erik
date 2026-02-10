package com.example.myapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private const val BASE_URL = "https://fakestoreapi.com/products/"
    //siempre tiene que terminar con /

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)    //url base
            .addConverterFactory(GsonConverterFactory.create()) // json -> kotlin
            .build() // construir
            .create(apiService::class.java) //crear la interfaz

    }
}