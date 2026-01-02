package com.latihan.latihansplashscreen

import android.content.Intent
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
    private lateinit var tvUsernameHeader: TextView
    private lateinit var tvUsername: TextView
    private lateinit var switchDarkMode: SwitchCompat
    private lateinit var btnAbout: Button
    private lateinit var btnLogout: Button
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize session manager
        sessionManager = SessionManager(requireContext())
        
        // Initialize views
        tvUsernameHeader = view.findViewById(R.id.tv_username_header)
        tvUsername = view.findViewById(R.id.tv_username)
        switchDarkMode = view.findViewById(R.id.switch_dark_mode)
        btnAbout = view.findViewById(R.id.btn_about)
        btnLogout = view.findViewById(R.id.btn_logout)
        
        // Animation References
        val cvProfilePic = view.findViewById<View>(R.id.cv_profile_pic)
        val cardInfo = view.findViewById<View>(R.id.card_info)
        val cardSettings = view.findViewById<View>(R.id.card_settings)
        val layoutActions = view.findViewById<View>(R.id.layout_actions)

        // Reset positions
        cvProfilePic.alpha = 0f
        cvProfilePic.scaleX = 0.5f
        cvProfilePic.scaleY = 0.5f
        
        cardInfo.alpha = 0f
        cardInfo.translationY = 100f
        
        cardSettings.alpha = 0f
        cardSettings.translationY = 100f
        
        layoutActions.alpha = 0f
        layoutActions.translationY = 50f

        // Animate
        cvProfilePic.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(600).start()
        tvUsernameHeader.animate().alpha(1f).setDuration(600).start()
        
        cardInfo.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(100).start()
        cardSettings.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(200).start()
        layoutActions.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(300).start()
        
        // Set user data
        val username = sessionManager.getUsername()
        tvUsernameHeader.text = username
        tvUsername.text = username
        
        // Set dark mode switch state
        switchDarkMode.isChecked = sessionManager.isDarkMode()
        
        // Set dark mode switch listener
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.saveDarkMode(isChecked)
            (activity as? MainActivity)?.applyTheme()
        }
        
        // Set about button listener
        btnAbout.setOnClickListener {
            val intent = Intent(requireActivity(), AboutActivity::class.java)
            startActivity(intent)
        }
        
        // Set logout button listener
        btnLogout.setOnClickListener {
            sessionManager.logout()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
