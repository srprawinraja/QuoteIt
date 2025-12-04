package com.example.quoteit.viewModels

import androidx.lifecycle.ViewModel
import com.example.quoteit.db.saved.SavedQuoteRepository

class SavedDetailViewModel (savedQuoteRepository: SavedQuoteRepository): ViewModel(){
    val savedQuoteFlow = savedQuoteRepository.savedQuoteFlow
}