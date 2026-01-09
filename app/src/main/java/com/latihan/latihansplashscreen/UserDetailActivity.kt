package com.latihan.latihansplashscreen

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class UserDetailActivity : AppCompatActivity() {

    private lateinit var ivAvatarLarge: ImageView
    private lateinit var tvUserId: TextView
    private lateinit var tvFullName: TextView
    private lateinit var tvEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail User"

        // Initialize views
        ivAvatarLarge = findViewById(R.id.iv_avatar_large)
        tvUserId = findViewById(R.id.tv_user_id)
        tvFullName = findViewById(R.id.tv_full_name)
        tvEmail = findViewById(R.id.tv_email)

        // Get data from intent
        val userId = intent.getIntExtra("USER_ID", 0)
        val firstName = intent.getStringExtra("USER_FIRST_NAME") ?: ""
        val lastName = intent.getStringExtra("USER_LAST_NAME") ?: ""
        val email = intent.getStringExtra("USER_EMAIL") ?: ""
        val avatar = intent.getStringExtra("USER_AVATAR") ?: ""

        // Set data to views
        tvUserId.text = userId.toString()
        tvFullName.text = "$firstName $lastName"
        tvEmail.text = email

        // Load large avatar with Glide
        Glide.with(this)
            .load(avatar)
            .circleCrop()
            .placeholder(R.drawable.bg_circle_soft)
            .into(ivAvatarLarge)

        // Animations
        val cardUserInfo = findViewById<android.view.View>(R.id.card_user_info)
        
        ivAvatarLarge.alpha = 0f
        ivAvatarLarge.scaleX = 0.5f
        ivAvatarLarge.scaleY = 0.5f
        ivAvatarLarge.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(600).start()
        
        cardUserInfo.alpha = 0f
        cardUserInfo.translationY = 100f
        cardUserInfo.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(200).start()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
