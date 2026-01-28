package com.prawin.quoteit.api

sealed class NetworkResponse<out T>{
    data class Success<out T>(val data:T):NetworkResponse<T>()
    data class ErrorQuote<out T>(val data:T, val message: String):NetworkResponse<T>()
    data class LoadingQuote<out T>(val data:T):NetworkResponse<T>()
    object Loading:NetworkResponse<Nothing>()
    data class Error(val message: String):NetworkResponse<Nothing>()
}
