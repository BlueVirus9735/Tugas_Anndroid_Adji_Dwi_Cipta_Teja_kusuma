package com.latihan.latihansplashscreen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    
    private lateinit var sessionManager: SessionManager
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // Initialize session manager
        sessionManager = SessionManager(this)
        
        // Initialize views
        editTextUsername = findViewById(R.id.edit_text_username)
        editTextPassword = findViewById(R.id.edit_text_password)
        buttonLogin = findViewById(R.id.button_login)
        
        // Set login button click listener
        buttonLogin.setOnClickListener {
            handleLogin()
        }
    }
    
    private fun handleLogin() {
        val username = editTextUsername.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        
        // Validasi input
        if (username.isEmpty()) {
            editTextUsername.error = "Username tidak boleh kosong"
            editTextUsername.requestFocus()
            return
        }
        
        if (password.isEmpty()) {
            editTextPassword.error = "Password tidak boleh kosong"
            editTextPassword.requestFocus()
            return
        }
        
        // Validasi username dan password dengan database
        val validUsername = "adji"
        val validPassword = "adjidwi123"
        
        if (username == validUsername && password == validPassword) {
            // Login berhasil
            sessionManager.saveLoginSession(username)
            
            Toast.makeText(this, "Login berhasil! Selamat datang $username", Toast.LENGTH_SHORT).show()
            
            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Login gagal
            Toast.makeText(this, "Username atau password salah!", Toast.LENGTH_SHORT).show()
            editTextPassword.text?.clear()
            editTextPassword.requestFocus()
        }
    }
}
