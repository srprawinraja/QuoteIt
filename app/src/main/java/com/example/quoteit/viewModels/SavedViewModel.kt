package com.example.quoteit.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteit.db.saved.SavedQuoteEntity
import com.example.quoteit.db.saved.SavedQuoteRepository
import kotlinx.coroutines.launch

class SavedViewModel (savedQuoteRepository: SavedQuoteRepository): ViewModel() {
    private val TAG: String = "SavedViewModel"
    val savedQuoteFlow = savedQuoteRepository.savedQuoteFlow

    fun getTagBasedSavedQuotes(tag: String): List<SavedQuoteEntity> {
        val tagBasedQuote: MutableList<SavedQuoteEntity> = mutableListOf()
        for(data in savedQuoteFlow.value){
            if(data.savedTagName.equals(tag)){
                tagBasedQuote.add(data)
            }
        }
        Log.d(TAG, tag+" tag based quotes retrieved "+tagBasedQuote)
        return tagBasedQuote
    }

    fun getSavedTags(): MutableSet<String> {
        val savedTags: MutableSet<String> = mutableSetOf()
        for(data in savedQuoteFlow.value){
            savedTags.add(data.savedTagName)
        }
        return savedTags
    }

}