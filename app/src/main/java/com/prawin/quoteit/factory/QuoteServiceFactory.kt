package com.prawin.quoteit.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prawin.quoteit.data.Quote
import com.prawin.quoteit.db.saved.SavedQuoteRepository
import com.prawin.quoteit.db.tag.TagRepository
import com.prawin.quoteit.utils.ContextHelper
import com.prawin.quoteit.utils.GsonHelper
import com.prawin.quoteit.utils.SharedPreferenceHelper
import com.prawin.quoteit.viewModels.QuoteShowViewModel
import com.prawin.quoteit.viewModels.HomeViewModel
import com.prawin.quoteit.viewModels.SavedDetailViewModel
import com.prawin.quoteit.viewModels.SavedViewModel
import com.prawin.quoteit.viewModels.TagsViewModel


class QuoteServiceFactory( private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

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