package com.latihan.latihansplashscreen

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    
    private lateinit var sessionManager: SessionManager
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mainLayout: ConstraintLayout
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize session manager
        sessionManager = SessionManager(this)
        
        // Initialize views
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        mainLayout = findViewById(R.id.main)
        
        // Apply dark mode if enabled
        applyTheme()
        
        // Set up bottom navigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_weather -> {
                    loadFragment(WeatherFragment())
                    true
                }
                R.id.nav_data -> {
                    loadFragment(DataFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    fun applyTheme() {
        val isDarkMode = sessionManager.isDarkMode()
        if (isDarkMode) {
            mainLayout.setBackgroundColor(Color.parseColor("#121212"))
        } else {
            mainLayout.setBackgroundColor(Color.WHITE)
        }
    }
}