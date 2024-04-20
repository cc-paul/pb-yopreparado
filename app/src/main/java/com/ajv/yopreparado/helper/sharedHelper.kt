package com.ajv.yopreparado.helper

import android.content.Context
import android.content.SharedPreferences
import com.ajv.yopreparado.MyApp

object sharedHelper {
    private const val PREFERENCES_NAME = "yopreparado_preferences"
    private val sharedPreferences: SharedPreferences = MyApp.instance.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }
}