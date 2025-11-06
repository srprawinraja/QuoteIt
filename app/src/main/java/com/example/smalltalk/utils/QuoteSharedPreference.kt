package com.example.smalltalk.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider

class QuoteSharedPreference(context: Context){
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("quote_prefs", Context.MODE_PRIVATE)

    fun save(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun getValue(key: String): String {
        return sharedPreferences.getString(key, null) ?:""
    }
    fun contains(key: String): Boolean {
        return sharedPreferences.getString(key, null)!=null
    }
}