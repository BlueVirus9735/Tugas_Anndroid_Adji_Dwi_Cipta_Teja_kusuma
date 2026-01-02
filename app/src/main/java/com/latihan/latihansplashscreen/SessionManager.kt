package com.latihan.latihansplashscreen

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREF_NAME = "UserSession"
        private const val KEY_IS_LOGIN = "is_login"
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
    }
    
    // Login functions
    fun saveLoginSession(username: String) {
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGIN, true)
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }
    
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGIN, false)
    }
    
    fun getUsername(): String {
        return prefs.getString(KEY_USERNAME, "") ?: ""
    }
    
    fun logout() {
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGIN, false)
        editor.remove(KEY_USERNAME)
        editor.apply()
    }
    
    // Dark mode functions
    fun saveDarkMode(isDarkMode: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_DARK_MODE, isDarkMode)
        editor.apply()
    }
    
    fun isDarkMode(): Boolean {
        return prefs.getBoolean(KEY_IS_DARK_MODE, false)
    }
}
