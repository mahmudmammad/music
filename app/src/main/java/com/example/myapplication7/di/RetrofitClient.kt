package com.example.myapplication7.di

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers(
        "x-rapidapi-key: 5d328dc1e7mshd29d2f619c35d09p1fab93jsn1dac6dfe6940",
        "x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com"
    )
    @GET("search")
    fun searchTracks(@Query("q") query: String): Call<DeezerResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://api.deezer.com/"

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
