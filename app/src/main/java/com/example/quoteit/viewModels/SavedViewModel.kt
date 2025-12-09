package com.example.quoteit.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteit.db.saved.SavedQuoteRepository
import kotlinx.coroutines.launch

class SavedViewModel (savedQuoteRepository: SavedQuoteRepository): ViewModel() {
    val savedQuoteFlow = savedQuoteRepository.savedQuoteFlow


}