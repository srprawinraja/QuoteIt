package com.prawin.quoteit.viewModels

import androidx.lifecycle.ViewModel
import com.prawin.quoteit.db.saved.SavedQuoteEntity
import com.prawin.quoteit.db.saved.SavedQuoteRepository

class SavedDetailViewModel (savedQuoteRepository: SavedQuoteRepository): ViewModel(){
    val savedQuoteFlow = savedQuoteRepository.savedQuoteFlow
    fun getTagBasedSavedQuotes(tag: String): List<SavedQuoteEntity> {
        val tagBasedQuote: MutableList<SavedQuoteEntity> = mutableListOf()
        for(data in savedQuoteFlow.value){
            if(data.savedTagName.equals(tag)){
                tagBasedQuote.add(data)
            }
        }
        return tagBasedQuote
    }
}