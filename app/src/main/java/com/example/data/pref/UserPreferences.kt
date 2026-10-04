package com.example.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("astram_hmt_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CUSTOM_API_KEY = "custom_gemini_api_key"
        private const val KEY_SELECTED_MODEL = "selected_gemini_model"
        private const val KEY_HIGH_THINKING = "high_thinking_enabled"
        private const val KEY_SELECTED_LANGUAGE = "selected_app_language"
        private const val KEY_SPEECH_SPEED = "speech_speed"
        private const val KEY_COSMIC_HAPTICS = "cosmic_haptics"
    }

    var customApiKey: String
        get() = prefs.getString(KEY_CUSTOM_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_CUSTOM_API_KEY, value).apply()

    fun getEffectiveApiKey(): String {
        val userKey = customApiKey.trim()
        if (userKey.isNotEmpty() && userKey != "MY_GEMINI_API_KEY") {
            return userKey
        }
        val buildKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }
        return if (buildKey.isNotEmpty() && buildKey != "MY_GEMINI_API_KEY") buildKey else ""
    }

    var selectedModel: String
        get() = prefs.getString(KEY_SELECTED_MODEL, "gemini-3.5-flash") ?: "gemini-3.5-flash"
        set(value) = prefs.edit().putString(KEY_SELECTED_MODEL, value).apply()

    var isHighThinkingEnabled: Boolean
        get() = prefs.getBoolean(KEY_HIGH_THINKING, false)
        set(value) = prefs.edit().putBoolean(KEY_HIGH_THINKING, value).apply()

    var selectedLanguage: String
        get() = prefs.getString(KEY_SELECTED_LANGUAGE, "English") ?: "English"
        set(value) = prefs.edit().putString(KEY_SELECTED_LANGUAGE, value).apply()

    var speechSpeed: Float
        get() = prefs.getFloat(KEY_SPEECH_SPEED, 1.0f)
        set(value) = prefs.edit().putFloat(KEY_SPEECH_SPEED, value).apply()

    var isCosmicHapticsEnabled: Boolean
        get() = prefs.getBoolean(KEY_COSMIC_HAPTICS, true)
        set(value) = prefs.edit().putBoolean(KEY_COSMIC_HAPTICS, value).apply()
}
