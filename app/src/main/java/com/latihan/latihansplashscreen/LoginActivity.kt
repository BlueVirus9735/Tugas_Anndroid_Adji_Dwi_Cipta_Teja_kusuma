package com.latihan.latihansplashscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)

        val cardLogin = findViewById<View>(R.id.card_login)
        val logoLogin = findViewById<View>(R.id.logo_login)
        val tvAppName = findViewById<View>(R.id.tv_app_name)
        
        logoLogin.alpha = 0f
        logoLogin.translationY = -50f
        logoLogin.animate().alpha(1f).translationY(0f).setDuration(800).start()
        
        tvAppName.alpha = 0f
        tvAppName.translationY = -30f
        tvAppName.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(100).start()
        
        cardLogin.alpha = 0f
        cardLogin.scaleX = 0.9f
        cardLogin.scaleY = 0.9f
        cardLogin.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(600).setStartDelay(300).start()

        loginBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username == "adji" && password == "adjidwi123") {
                sessionManager.saveLoginSession(username)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login Gagal. Cek username dan password!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
