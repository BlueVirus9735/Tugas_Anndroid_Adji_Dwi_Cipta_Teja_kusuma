package com.latihan.latihansplashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

class HomeFragment : Fragment() {
    
    // UI Components
    private lateinit var etPanjang: TextInputEditText
    private lateinit var etLebar: TextInputEditText
    private lateinit var btnHitung: Button
    private lateinit var tvHasil: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize views
        etPanjang = view.findViewById(R.id.et_panjang)
        etLebar = view.findViewById(R.id.et_lebar)
        btnHitung = view.findViewById(R.id.btn_hitung)
        tvHasil = view.findViewById(R.id.tv_hasil)
        
        // Animation References
        val cardCalculator = view.findViewById<View>(R.id.card_calculator)
        val cardResult = view.findViewById<View>(R.id.card_result)
        val tvTitle = view.findViewById<View>(R.id.tv_title)
        val tvSubtitle = view.findViewById<View>(R.id.tv_subtitle)

        // Reset positions for animation
        cardCalculator.alpha = 0f
        cardCalculator.translationY = 100f
        cardResult.alpha = 0f
        cardResult.translationY = 100f
        tvTitle.alpha = 0f
        tvTitle.translationY = -50f
        tvSubtitle.alpha = 0f

        // Execute Animations
        tvTitle.animate().alpha(1f).translationY(0f).setDuration(500).start()
        tvSubtitle.animate().alpha(1f).setDuration(500).setStartDelay(100).start()
        
        cardCalculator.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(200)
            .start()
            
        cardResult.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(300)
            .start()
        
        // Set button click listener
        btnHitung.setOnClickListener {
            hitungLuas()
        }
    }
    
    private fun hitungLuas() {
        val panjangStr = etPanjang.text.toString()
        val lebarStr = etLebar.text.toString()
        
        // Validasi input
        if (panjangStr.isEmpty()) {
            etPanjang.error = "Panjang tidak boleh kosong"
            etPanjang.requestFocus()
            return
        }
        
        if (lebarStr.isEmpty()) {
            etLebar.error = "Lebar tidak boleh kosong"
            etLebar.requestFocus()
            return
        }
        
        // Hitung luas
        val panjang = panjangStr.toDoubleOrNull()
        val lebar = lebarStr.toDoubleOrNull()
        
        if (panjang == null || lebar == null) {
            tvHasil.text = "Error"
            return
        }
        
        val luas = panjang * lebar
        
        // Format hasil (hapus .0 jika bilangan bulat)
        val luasFormatted = if (luas % 1.0 == 0.0) {
            String.format("%.0f", luas)
        } else {
            String.format("%.2f", luas)
        }
        
        tvHasil.text = luasFormatted
    }
}
