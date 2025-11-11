package com.example.quoteit.viewModels

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteit.DEFAULT_AUTHOR_NAME
import com.example.quoteit.DEFAULT_TAG
import com.example.quoteit.ERROR_MESSAGE
import com.example.quoteit.INTERNET_TURN_ON_REQUEST_MESSAGE
import com.example.quoteit.LOADING_MESSAGE
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.api.RetroFitInstance
import com.example.quoteit.data.Quote
import com.example.quoteit.utils.ContextHelper
import com.example.quoteit.utils.DateHelper
import com.example.quoteit.utils.GsonHelper
import com.example.quoteit.utils.NetworkHelper
import com.example.quoteit.utils.SharedPreferenceHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class QuoteViewModel(contextHelper: ContextHelper,
                     val sharedPreferenceHelper: SharedPreferenceHelper,
                     val gsonHelper: GsonHelper<Quote>): ViewModel()  {

    val defaultErrorQuote =  Quote(
        "1",
        DEFAULT_AUTHOR_NAME,
        ERROR_MESSAGE,
        listOf(DEFAULT_TAG)
    )
    val defaultLoadingQuote =  Quote(
        "2",
        DEFAULT_AUTHOR_NAME,
        LOADING_MESSAGE,
        listOf(DEFAULT_TAG)
    )
    private val _uiState = MutableStateFlow<NetworkResponse<Quote>>(NetworkResponse.LoadingQuote(defaultLoadingQuote))
    val uiState: MutableStateFlow<NetworkResponse<Quote>> = _uiState
    val quoteService = RetroFitInstance.quoteService
    val todayDate: String = DateHelper.getDate()
    private val networkHelper: NetworkHelper = NetworkHelper(contextHelper){
        getQuote()
    }


    init{
        getQuote()
    }

    fun getQuote() {
        if(sharedPreferenceHelper.contains(todayDate)) {
            val json = sharedPreferenceHelper.getValue(todayDate)
            val data = gsonHelper.getObj(json, Quote::class.java)
            _uiState.value = NetworkResponse.Success(data)
        }
        else {
            if(networkHelper.isNetworkAvailable()) {
                networkHelper.stopMonitoring()
                viewModelScope.launch {
                    val response: Response<Quote> = quoteService.getRandomQuote()
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _uiState.value = NetworkResponse.Success(body)
                            val json = gsonHelper.getJson(body)
                            sharedPreferenceHelper.save(todayDate, json)
                        } else {
                            _uiState.value = NetworkResponse.ErrorQuote(defaultErrorQuote, response.message())
                        }
                    } else {
                        Log.e("error", response.message())
                        _uiState.value = NetworkResponse.ErrorQuote(defaultErrorQuote, response.message())
                    }
                }
            } else {
                _uiState.value = NetworkResponse.LoadingQuote(
                    defaultLoadingQuote.copy(content = INTERNET_TURN_ON_REQUEST_MESSAGE)
                )
                networkHelper.startMonitoring()
            }
        }
    }
}


