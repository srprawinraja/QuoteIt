package com.example.smalltalk.utils

import android.R
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


object SharedPreferenceUtility {
    const val FILE_NAME = "myPrefs"

    public fun save(activity: Activity, key:String, value:String){
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }
    public fun get(activity: Activity, key:String):String{
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key, "")?:""
    }
    public fun isContains(activity: Activity, key:String): Boolean{
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key, "")!=""
    }
}