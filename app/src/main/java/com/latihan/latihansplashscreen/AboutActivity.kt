package com.latihan.latihansplashscreen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar?.hide()

        val cardAbout = findViewById<View>(R.id.card_about)
        val tvTitle = findViewById<View>(R.id.tv_title)
        val tvSubtitle = findViewById<View>(R.id.tv_subtitle)
        
        cardAbout.alpha = 0f
        cardAbout.translationY = 100f
        
        tvTitle.alpha = 0f
        tvTitle.translationY = -50f
        
        tvSubtitle.alpha = 0f
        
        tvTitle.animate().alpha(1f).translationY(0f).setDuration(500).start()
        tvSubtitle.animate().alpha(1f).setDuration(500).setStartDelay(100).start()
        cardAbout.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(200).start()
    }
}
