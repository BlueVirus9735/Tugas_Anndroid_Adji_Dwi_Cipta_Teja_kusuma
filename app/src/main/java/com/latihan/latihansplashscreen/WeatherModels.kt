package com.latihan.latihansplashscreen

import com.google.gson.annotations.SerializedName

// Data class untuk response API cuaca
data class WeatherResponse(
    @SerializedName("latitude")
    val latitude: Double,
    
    @SerializedName("longitude")
    val longitude: Double,
    
    @SerializedName("current")
    val current: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature: Double,
    
    @SerializedName("relative_humidity_2m")
    val humidity: Int,
    
    @SerializedName("wind_speed_10m")
    val windSpeed: Double
)
