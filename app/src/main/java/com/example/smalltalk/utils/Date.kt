package com.example.smalltalk.utils

import java.time.LocalDate

object Date {
    fun getDate():String{
        return LocalDate.now().toString();
    }
}