package com.latihan.latihansplashscreen

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {
    
    private lateinit var sessionManager: SessionManager
    private lateinit var textViewWelcome: TextView
    private lateinit var switchModeGelap: SwitchCompat
    private lateinit var buttonAbout: Button
    private lateinit var buttonLogout: Button
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        
        // Initialize session manager
        sessionManager = SessionManager(requireContext())
        
        // Initialize views
        textViewWelcome = view.findViewById(R.id.text_view_welcome)
        switchModeGelap = view.findViewById(R.id.switch_mode_gelap)
        buttonAbout = view.findViewById(R.id.button_about)
        buttonLogout = view.findViewById(R.id.button_logout)
        
        // Set welcome message
        val username = sessionManager.getUsername()
        textViewWelcome.text = "Selamat datang, $username!"
        
        // Set dark mode switch state
        switchModeGelap.isChecked = sessionManager.isDarkMode()
        
        // Apply theme
        applyTheme(view)
        
        // Set dark mode switch listener
        switchModeGelap.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.saveDarkMode(isChecked)
            applyTheme(view)
            // Update MainActivity theme
            (activity as? MainActivity)?.applyTheme()
        }
        
        // Set about button listener
        buttonAbout.setOnClickListener {
            val intent = Intent(requireActivity(), AboutActivity::class.java)
            startActivity(intent)
        }
        
        // Set logout button listener
        buttonLogout.setOnClickListener {
            sessionManager.logout()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
        
        return view
    }
    
    private fun applyTheme(view: View) {
        val isDarkMode = sessionManager.isDarkMode()
        if (isDarkMode) {
            textViewWelcome.setTextColor(Color.WHITE)
        } else {
            textViewWelcome.setTextColor(Color.BLACK)
        }
    }
}
