package com.arikan.campusmenu.data

import android.content.Context
import android.content.SharedPreferences

object ThemePreferences {
    private const val PREF_NAME = "theme_preferences"
    private const val KEY_DARK_MODE = "dark_mode"
    private const val KEY_LANGUAGE = "language"
    private const val KEY_THEME = "theme"
    private const val KEY_DAILY_MENU_NOTIFICATION = "daily_menu_notification"
    private const val KEY_ANNOUNCEMENTS_NOTIFICATION = "announcements_notification"
    private const val KEY_CROWDING_WARNINGS = "crowding_warnings"
    
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    // Dark Mode
    fun isDarkMode(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_DARK_MODE, false)
    }
    
    fun setDarkMode(context: Context, isDark: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_DARK_MODE, isDark).apply()
    }
    
    fun toggleDarkMode(context: Context): Boolean {
        val current = isDarkMode(context)
        setDarkMode(context, !current)
        return !current
    }
    
    // Language
    fun getLanguage(context: Context): String {
        return getPreferences(context).getString(KEY_LANGUAGE, "Türkçe") ?: "Türkçe"
    }
    
    fun setLanguage(context: Context, language: String) {
        getPreferences(context).edit().putString(KEY_LANGUAGE, language).apply()
    }
    
    // Theme
    fun getTheme(context: Context): String {
        return getPreferences(context).getString(KEY_THEME, "Turuncu") ?: "Turuncu"
    }
    
    fun setTheme(context: Context, theme: String) {
        getPreferences(context).edit().putString(KEY_THEME, theme).apply()
    }
    
    // Notifications
    fun getDailyMenuNotification(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_DAILY_MENU_NOTIFICATION, true)
    }
    
    fun setDailyMenuNotification(context: Context, enabled: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_DAILY_MENU_NOTIFICATION, enabled).apply()
    }
    
    fun getAnnouncementsNotification(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_ANNOUNCEMENTS_NOTIFICATION, true)
    }
    
    fun setAnnouncementsNotification(context: Context, enabled: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_ANNOUNCEMENTS_NOTIFICATION, enabled).apply()
    }
    
    fun getCrowdingWarnings(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_CROWDING_WARNINGS, false)
    }
    
    fun setCrowdingWarnings(context: Context, enabled: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_CROWDING_WARNINGS, enabled).apply()
    }
}
