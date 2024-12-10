package com.example.myapplication7.di



import com.example.myapplication7.data.model.Data
import com.example.myapplication7.data.model.MyData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query



data class DeezerResponse(
    val data: List<Data>
)
