package com.example.quoteit.viewModels

import com.example.quoteit.db.saved.SavedQuoteRepository

class SavedDetailViewModel (savedQuoteRepository: SavedQuoteRepository){
    val savedQuoteFlow = savedQuoteRepository.savedQuoteFlow

}