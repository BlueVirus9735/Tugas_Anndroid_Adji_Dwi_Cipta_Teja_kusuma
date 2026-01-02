package com.latihan.latihansplashscreen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {
    
    private lateinit var sessionManager: SessionManager
    private lateinit var editTextPanjangPersegi: EditText
    private lateinit var editTextLebarPersegi: EditText
    private lateinit var buttonHitungLuas: Button
    private lateinit var textViewHasilLuas: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        
        // Initialize session manager
        sessionManager = SessionManager(requireContext())
        
        // Initialize views
        editTextPanjangPersegi = view.findViewById(R.id.edit_text_panjang_persegi)
        editTextLebarPersegi = view.findViewById(R.id.edit_text_lebar_persegi)
        buttonHitungLuas = view.findViewById(R.id.button_hitung_luas)
        textViewHasilLuas = view.findViewById(R.id.text_view_hasil_luas)
        
        // Apply theme
        applyTheme(view)
        
        // Set button click listener
        buttonHitungLuas.setOnClickListener {
            hitungLuas()
        }
        
        return view
    }
    
    private fun hitungLuas() {
        val panjangStr = editTextPanjangPersegi.text.toString()
        val lebarStr = editTextLebarPersegi.text.toString()
        
        // Validasi input
        if (panjangStr.isEmpty()) {
            editTextPanjangPersegi.error = "Panjang tidak boleh kosong"
            editTextPanjangPersegi.requestFocus()
            return
        }
        
        if (lebarStr.isEmpty()) {
            editTextLebarPersegi.error = "Lebar tidak boleh kosong"
            editTextLebarPersegi.requestFocus()
            return
        }
        
        // Hitung luas
        val panjang = panjangStr.toDoubleOrNull()
        val lebar = lebarStr.toDoubleOrNull()
        
        if (panjang == null || lebar == null) {
            textViewHasilLuas.text = "Input harus berupa angka!"
            return
        }
        
        val luas = panjang * lebar
        textViewHasilLuas.text = "Luas: $luas"
    }
    
    private fun applyTheme(view: View) {
        val isDarkMode = sessionManager.isDarkMode()
        if (isDarkMode) {
            textViewHasilLuas.setTextColor(Color.WHITE)
        } else {
            textViewHasilLuas.setTextColor(Color.BLACK)
        }
    }
}
