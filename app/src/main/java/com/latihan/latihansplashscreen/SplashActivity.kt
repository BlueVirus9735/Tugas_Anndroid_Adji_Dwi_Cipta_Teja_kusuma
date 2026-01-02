package com.latihan.latihansplashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    
    private lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // Initialize session manager
        sessionManager = SessionManager(this)
        
        // Keep splash screen visible while checking login status
        splashScreen.setKeepOnScreenCondition { true }
        
        // Check login status and navigate
        checkLoginStatus()
    }
    
    private fun checkLoginStatus() {
        // Simulate splash screen delay
        android.os.Handler(mainLooper).postDelayed({
            if (sessionManager.isLoggedIn()) {
                // User is logged in, go to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // User is not logged in, go to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000) // 2 seconds delay
    }
}
