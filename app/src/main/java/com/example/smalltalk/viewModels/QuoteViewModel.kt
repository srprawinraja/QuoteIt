package com.example.smalltalk.viewModels

import android.R
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smalltalk.api.RetroFitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class QuoteViewModel(): ViewModel()  {
    init{
        getQuote()
    }

    val retrofit = RetroFitInstance.service
    private val _uiState = MutableStateFlow("")
    val uiState: StateFlow<String> = _uiState
    fun getQuote() {
        viewModelScope.launch {
            val quote = retrofit.getRandomQuote()
           _uiState.value=quote.content
        }
    }


}

