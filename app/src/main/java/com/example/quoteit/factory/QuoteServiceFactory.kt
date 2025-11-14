package com.example.quoteit.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quoteit.data.Quote
import com.example.quoteit.db.tag.TagRepository
import com.example.quoteit.utils.ContextHelper
import com.example.quoteit.utils.GsonHelper
import com.example.quoteit.utils.SharedPreferenceHelper
import com.example.quoteit.viewModels.QuoteShowViewModel
import com.example.quoteit.viewModels.HomeViewModel
import com.example.quoteit.viewModels.TagsViewModel
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class QuoteServiceFactory( private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.TAMIL)
            .build()
        val translator = Translation.getClient(options)
        translator.downloadModelIfNeeded()

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(ContextHelper(context), SharedPreferenceHelper(context), GsonHelper<Quote>(),
                TagRepository(context), translator) as T
        } else if (modelClass.isAssignableFrom(QuoteShowViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuoteShowViewModel(ContextHelper(context)) as T
        }  else if (modelClass.isAssignableFrom(TagsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagsViewModel(TagRepository(context)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}