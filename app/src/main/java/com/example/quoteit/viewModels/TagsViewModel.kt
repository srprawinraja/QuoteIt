package com.example.quoteit.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.api.RetroFitInstance
import com.example.quoteit.db.tag.TagRepository
import com.example.quoteit.db.tag.TagEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TagsViewModel (val tagRepository: TagRepository): ViewModel() {
    val tagsFlow = tagRepository.tagsFlow
    fun changeMarked(id: Int, marked: Boolean){
        viewModelScope.launch {
            tagRepository.updateMarked(id, marked)
        }
    }
}