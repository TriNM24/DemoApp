package android.com.demo.utils

import android.content.Context
import java.util.*

object PreferenceHelper {
    private const val DEFAULT_PREFERENCE = "default_preference"
    private const val SETTING_TIME = "setting_time"

    fun saveStringData(context: Context, key: String, valueStr: String) {
        val sharedPref =
            context.getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(key, valueStr)
            apply()
        }
    }

    fun getStringData(context: Context, key: String): String? {
        val sharedPref =
            context.getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_PRIVATE) ?: return null
        return sharedPref.getString(key, null)
    }
}