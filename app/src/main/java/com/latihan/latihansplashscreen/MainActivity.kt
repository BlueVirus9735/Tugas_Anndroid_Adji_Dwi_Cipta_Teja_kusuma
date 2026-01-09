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

        sessionManager = SessionManager(this)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        mainLayout = findViewById(R.id.main)

        applyTheme()
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
                    loadFragment(DataContainerFragment())
                    true
                }
                R.id.nav_maps -> {
                    loadFragment(MapsFragment())
                    true
                }
                R.id.nav_ticket -> {
                    loadFragment(TicketFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
        handleNotificationIntent()
    }
    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent()
    }
    
    private fun handleNotificationIntent() {
        val showDialog = intent.getBooleanExtra("show_dialog", false)
        if (showDialog) {
            val nama = intent.getStringExtra("nama_pemesan") ?: "-"
            val jumlah = intent.getIntExtra("jumlah_tiket", 0)
            
            showTicketDialog(nama, jumlah)
            intent.removeExtra("show_dialog")
        }
    }
    
    private fun showTicketDialog(nama: String, jumlah: Int) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Detail Pesanan")
            .setMessage("Nama: $nama\nJumlah Tiket: $jumlah\n\nStatus: Berhasil Dipesan")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
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