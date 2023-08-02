package com.setiawanalraz.finalprojectgigihme.api.client

import com.google.gson.GsonBuilder
import com.setiawanalraz.finalprojectgigihme.api.services.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val gson = GsonBuilder().setLenient().create()
    private const val BASE_URL = "https://data.petabencana.id/"

    val instance: ApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }
        retrofit.create(ApiService::class.java)
    }
}