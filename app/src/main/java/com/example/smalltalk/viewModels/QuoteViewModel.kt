package com.example.smalltalk.viewModels

import android.R
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smalltalk.api.RetroFitInstance
import com.example.smalltalk.utils.Date
import com.example.smalltalk.utils.QuoteSharedPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf


class QuoteViewModel(val quoteSharedPreference: QuoteSharedPreference): ViewModel()  {

    private val _uiState = MutableStateFlow("")
    val uiState: StateFlow<String> = _uiState
    val quoteService = RetroFitInstance.quoteService

    init{
        getQuote()
    }
    fun getQuote() {
        val todayDate: String = Date.getDate()
        if(quoteSharedPreference.contains(todayDate)) {
            _uiState.value =  quoteSharedPreference.getValue(todayDate)
        }
        else {
            viewModelScope.launch {
                val quote = quoteService.getRandomQuote()

                quoteSharedPreference.save(todayDate, quote.content)
                _uiState.value = quote.content

            }

        }

        }

}


