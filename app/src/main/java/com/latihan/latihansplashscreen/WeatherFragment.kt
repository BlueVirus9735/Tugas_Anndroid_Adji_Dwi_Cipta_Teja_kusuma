package com.latihan.latihansplashscreen

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
    
    private lateinit var tvLocation: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var btnRefresh: Button
    private lateinit var progressBar: ProgressBar
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize views
        tvLocation = view.findViewById(R.id.tv_location)
        tvTemperature = view.findViewById(R.id.tv_temperature)
        tvHumidity = view.findViewById(R.id.tv_humidity)
        tvWindSpeed = view.findViewById(R.id.tv_wind_speed)
        btnRefresh = view.findViewById(R.id.btn_refresh)
        progressBar = view.findViewById(R.id.progress_bar)
        
        // Animation References
        val cardWeather = view.findViewById<View>(R.id.card_weather)
        val tvTitle = view.findViewById<View>(R.id.tv_title)
        
        // Setup Animations
        cardWeather.alpha = 0f
        cardWeather.translationY = 100f
        cardWeather.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(200).start()
        
        tvTitle.alpha = 0f
        tvTitle.translationY = -50f
        tvTitle.animate().alpha(1f).translationY(0f).setDuration(500).start()
        
        // Set button click listener
        btnRefresh.setOnClickListener {
            getWeatherData()
        }
        
        // Initial Fetch
        getWeatherData()
    }
    
    private fun getWeatherData() {
        // Jakarta coordinates
        val latitude = -6.2088
        val longitude = 106.8456
        
        progressBar.visibility = View.VISIBLE
        btnRefresh.isEnabled = false
        
        val call = RetrofitClient.weatherApiService.getCurrentWeather(latitude, longitude)
        
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (!isAdded) return // Check if fragment is still attached
                
                progressBar.visibility = View.GONE
                btnRefresh.isEnabled = true
                
                if (response.isSuccessful) {
                    val weather = response.body()
                    weather?.let {
                        tvLocation.text = "Jakarta"
                        tvTemperature.text = "${it.current.temperature}°C"
                        tvHumidity.text = "${it.current.humidity}%"
                        tvWindSpeed.text = "${it.current.windSpeed} km/h"
                    }
                } else {
                    Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }
            
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                if (!isAdded) return
                
                progressBar.visibility = View.GONE
                btnRefresh.isEnabled = true
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
