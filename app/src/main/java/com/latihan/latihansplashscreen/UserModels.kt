package com.latihan.latihansplashscreen

import com.google.gson.annotations.SerializedName

// Data class untuk single user
data class User(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("first_name")
    val firstName: String,
    
    @SerializedName("last_name")
    val lastName: String,
    
    @SerializedName("avatar")
    val avatar: String
)

// Data class untuk response API
data class UserResponse(
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("per_page")
    val perPage: Int,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("total_pages")
    val totalPages: Int,
    
    @SerializedName("data")
    val data: List<User>
)
