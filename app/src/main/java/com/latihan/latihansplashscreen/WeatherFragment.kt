package com.latihan.latihansplashscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class City(val name: String, val lat: Double, val lon: Double)

class WeatherFragment : Fragment() {

    private lateinit var tvTemperature: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var btnRefresh: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var spinnerKota: Spinner

    private var currentLat = -6.2088
    private var currentLon = 106.8456

    private val cities = listOf(

        City("Jakarta", -6.2088, 106.8456),
        City("Bandung", -6.9175, 107.6191),
        City("Surabaya", -7.2575, 112.7521),
        City("Yogyakarta", -7.7955, 110.3695),
        City("Bali", -8.4095, 115.1889),
        City("Medan", 3.5952, 98.6722),
        City("Makassar", -5.1477, 119.4327),
        City("Semarang", -6.9667, 110.4167),
        City("London", 51.5074, -0.1278),
        City("New York", 40.7128, -74.0060),
        City("Tokyo", 35.6762, 139.6503)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTemperature = view.findViewById(R.id.tv_temperature)
        tvLocation = view.findViewById(R.id.tv_location)
        tvHumidity = view.findViewById(R.id.tv_humidity)
        tvWindSpeed = view.findViewById(R.id.tv_wind_speed)
        btnRefresh = view.findViewById(R.id.btn_refresh)
        progressBar = view.findViewById(R.id.progress_bar)
        spinnerKota = view.findViewById(R.id.spinner_kota)

        val cardWeather = view.findViewById<View>(R.id.card_weather)
        val tvTitle = view.findViewById<View>(R.id.tv_title)
        
        cardWeather.alpha = 0f
        cardWeather.translationY = 50f
        cardWeather.animate().alpha(1f).translationY(0f).setDuration(600).start()
        
        tvTitle.alpha = 0f
        tvTitle.translationY = -30f
        tvTitle.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(100).start()

        setupSpinner()

        fetchWeatherData(currentLat, currentLon)

        btnRefresh.setOnClickListener {
            fetchWeatherData(currentLat, currentLon)
        }
    }
    
    private fun setupSpinner() {
        val cityNames = cities.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cityNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKota.adapter = adapter

        spinnerKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = cities[position]
                currentLat = selectedCity.lat
                currentLon = selectedCity.lon
                tvLocation.text = selectedCity.name
                fetchWeatherData(currentLat, currentLon)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun fetchWeatherData(lat: Double, lon: Double) {
        showLoading(true)

        val call = RetrofitClient.weatherApiService.getCurrentWeather(lat, lon)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (!isAdded) return
                showLoading(false)
                if (response.isSuccessful) {
                    val weather = response.body()
                    if (weather != null) {
                        tvTemperature.text = "${weather.current.temperature}°C"
                        tvWindSpeed.text = "${weather.current.windSpeed} km/h"
                        tvHumidity.text = "${weather.current.humidity}%"
                    }
                } else {
                    tvLocation.text = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                if (!isAdded) return
                showLoading(false)
                tvLocation.text = "Failed"
                Log.e("WeatherFragment", "Error: ${t.message}")
            }
        })
    }
    
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            btnRefresh.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            btnRefresh.isEnabled = true
        }
    }
}
