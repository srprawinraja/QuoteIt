package com.example.quoteit.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quoteit.data.Quote
import com.example.quoteit.db.saved.SavedQuoteRepository
import com.example.quoteit.db.tag.TagRepository
import com.example.quoteit.ui.screens.SavedDetailScreen
import com.example.quoteit.utils.ContextHelper
import com.example.quoteit.utils.GsonHelper
import com.example.quoteit.utils.NetworkHelper
import com.example.quoteit.utils.SharedPreferenceHelper
import com.example.quoteit.viewModels.QuoteShowViewModel
import com.example.quoteit.viewModels.HomeViewModel
import com.example.quoteit.viewModels.SavedDetailViewModel
import com.example.quoteit.viewModels.SavedViewModel
import com.example.quoteit.viewModels.TagsViewModel


class QuoteServiceFactory( private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       /* val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.TAMIL)
            .build()
        val translator = Translation.getClient(options)
        translator.downloadModelIfNeeded()*/
        val contextHelper = ContextHelper(context)
        val sharedPreferenceHelper =  SharedPreferenceHelper(context)
        val tagRepository = TagRepository(context)
        val gson = GsonHelper<Quote>()
        val savedQuoteRepository = SavedQuoteRepository(context)
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                contextHelper, sharedPreferenceHelper, gson,
                tagRepository,
                savedQuoteRepository
            ) as T
        } else if (modelClass.isAssignableFrom(QuoteShowViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuoteShowViewModel(contextHelper) as T
        }  else if (modelClass.isAssignableFrom(TagsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagsViewModel(tagRepository, contextHelper) as T
        } else if (modelClass.isAssignableFrom(SavedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SavedViewModel(savedQuoteRepository) as T
        } else if (modelClass.isAssignableFrom(SavedDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SavedDetailViewModel(savedQuoteRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}