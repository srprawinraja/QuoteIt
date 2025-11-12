package com.example.quoteit.factory

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quoteit.data.Quote
import com.example.quoteit.db.TagDatabaseService
import com.example.quoteit.utils.CacheImageHelper
import com.example.quoteit.utils.ContextHelper
import com.example.quoteit.utils.GsonHelper
import com.example.quoteit.utils.SharedPreferenceHelper
import com.example.quoteit.viewModels.QuoteShowViewModel
import com.example.quoteit.viewModels.QuoteViewModel
import com.example.quoteit.viewModels.TagsViewModel

class QuoteServiceFactory(private val application: Application, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(QuoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuoteViewModel(ContextHelper(context), SharedPreferenceHelper(context), GsonHelper<Quote>()) as T
        } else if (modelClass.isAssignableFrom(QuoteShowViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuoteShowViewModel(ContextHelper(context)) as T
        }  else if (modelClass.isAssignableFrom(TagsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagsViewModel(TagDatabaseService(application)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}