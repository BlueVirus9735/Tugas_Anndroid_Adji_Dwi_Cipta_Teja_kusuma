package com.latihan.latihansplashscreen

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {
    @GET("api/users")
    fun getUsers(@Query("page") page: Int): Call<UserResponse>
}
