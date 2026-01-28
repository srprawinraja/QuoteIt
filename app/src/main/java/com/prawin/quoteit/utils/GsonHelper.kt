package com.prawin.quoteit.utils

import com.google.gson.Gson

class GsonHelper <T>{
    private val gson = Gson()
    fun getJson(obj: T): String{
        return gson.toJson(obj)
    }
    fun getObj(json: String,  classOf: Class<T>):T{
        return gson.fromJson(json, classOf)
    }
}