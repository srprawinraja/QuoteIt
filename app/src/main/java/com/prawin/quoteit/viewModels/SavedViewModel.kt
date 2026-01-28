package com.prawin.quoteit.viewModels

import androidx.lifecycle.ViewModel
import com.prawin.quoteit.db.saved.SavedQuoteEntity
import com.prawin.quoteit.db.saved.SavedQuoteRepository

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
       // Log.d(TAG, tag+" tag based quotes retrieved "+tagBasedQuote)
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