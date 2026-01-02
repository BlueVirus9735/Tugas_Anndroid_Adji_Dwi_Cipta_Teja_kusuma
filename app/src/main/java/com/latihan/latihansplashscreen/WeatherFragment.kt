package com.latihan.latihansplashscreen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherFragment : Fragment() {
    
    private lateinit var sessionManager: SessionManager
    private lateinit var textViewLocation: TextView
    private lateinit var textViewTemperature: TextView
    private lateinit var textViewHumidity: TextView
    private lateinit var textViewWindSpeed: TextView
    private lateinit var buttonGetWeather: Button
    private lateinit var progressBar: ProgressBar
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        
        // Initialize session manager
        sessionManager = SessionManager(requireContext())
        
        // Initialize views
        textViewLocation = view.findViewById(R.id.text_view_location)
        textViewTemperature = view.findViewById(R.id.text_view_temperature)
        textViewHumidity = view.findViewById(R.id.text_view_humidity)
        textViewWindSpeed = view.findViewById(R.id.text_view_wind_speed)
        buttonGetWeather = view.findViewById(R.id.button_get_weather)
        progressBar = view.findViewById(R.id.progress_bar)
        
        // Apply theme
        applyTheme(view)
        
        // Set button click listener
        buttonGetWeather.setOnClickListener {
            getWeatherData()
        }
        
        return view
    }
    
    private fun getWeatherData() {
        // Jakarta coordinates
        val latitude = -6.2088
        val longitude = 106.8456
        
        progressBar.visibility = View.VISIBLE
        buttonGetWeather.isEnabled = false
        
        val call = RetrofitClient.weatherApiService.getCurrentWeather(latitude, longitude)
        
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                progressBar.visibility = View.GONE
                buttonGetWeather.isEnabled = true
                
                if (response.isSuccessful) {
                    val weather = response.body()
                    weather?.let {
                        textViewLocation.text = "Jakarta, Indonesia"
                        textViewTemperature.text = "Suhu: ${it.current.temperature}°C"
                        textViewHumidity.text = "Kelembaban: ${it.current.humidity}%"
                        textViewWindSpeed.text = "Kecepatan Angin: ${it.current.windSpeed} km/h"
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Gagal mengambil data cuaca",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                buttonGetWeather.isEnabled = true
                Toast.makeText(
                    requireContext(),
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    
    private fun applyTheme(view: View) {
        val isDarkMode = sessionManager.isDarkMode()
        if (isDarkMode) {
            textViewLocation.setTextColor(Color.WHITE)
            textViewTemperature.setTextColor(Color.WHITE)
            textViewHumidity.setTextColor(Color.WHITE)
            textViewWindSpeed.setTextColor(Color.WHITE)
        } else {
            textViewLocation.setTextColor(Color.BLACK)
            textViewTemperature.setTextColor(Color.BLACK)
            textViewHumidity.setTextColor(Color.BLACK)
            textViewWindSpeed.setTextColor(Color.BLACK)
        }
    }
}
