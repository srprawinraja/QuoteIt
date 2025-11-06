package com.example.smalltalk.factory

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smalltalk.api.QuotesService
import com.example.smalltalk.utils.QuoteSharedPreference
import com.example.smalltalk.viewModels.QuoteViewModel

class QuoteServiceFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuoteViewModel(QuoteSharedPreference(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}