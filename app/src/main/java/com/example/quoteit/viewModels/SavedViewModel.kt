package com.example.quoteit.viewModels

import com.example.quoteit.db.saved.SavedQuoteRepository

class SavedViewModel (savedQuoteRepository: SavedQuoteRepository){
    val savedQuoteFlow = savedQuoteRepository.savedQuoteFlow

}