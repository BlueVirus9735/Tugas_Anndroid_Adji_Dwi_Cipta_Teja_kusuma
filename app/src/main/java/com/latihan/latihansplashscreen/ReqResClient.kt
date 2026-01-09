package com.latihan.latihansplashscreen

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ReqResClient {
    private const val BASE_URL = "https://reqres.in/"
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val userApiService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}
