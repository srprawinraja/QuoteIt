package com.prawin.quoteit.utils

import java.time.LocalDate

object DateHelper {
    fun getDate():String{
        return LocalDate.now().toString();
    }
}