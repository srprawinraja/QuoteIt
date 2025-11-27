package com.example.quoteit.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.api.RetroFitInstance
import com.example.quoteit.api.RetroFitInstance.quoteService
import com.example.quoteit.data.TagsItem
import com.example.quoteit.db.tag.TagRepository
import com.example.quoteit.db.tag.TagEntity
import com.example.quoteit.utils.CacheImageHelper
import com.example.quoteit.utils.ContextHelper
import com.example.quoteit.utils.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response

class TagsViewModel (val tagRepository: TagRepository, contextHelper: ContextHelper): ViewModel() {
    private val networkHelper: NetworkHelper = NetworkHelper(contextHelper){
        getListOfTags()
    }
    val tagsFlow = tagRepository.tagsFlow
    init {
        getListOfTags()
    }
    fun changeTagMark(id: Int, marked: Boolean){
        viewModelScope.launch {
            tagRepository.updateMarked(id, marked)
        }
    }

    fun getListOfTags(){
        viewModelScope.launch {
            if(networkHelper.isNetworkAvailable()) {
                networkHelper.stopMonitoring()
                if (tagRepository.getAllTag().isEmpty()) {
                    val response: Response<ArrayList<TagsItem>> = quoteService.getAllTags()
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            val filteredOutTags = CacheImageHelper.filterOutInvalidTags(body)
                            tagRepository.insertAllTag(filteredOutTags)
                        }
                    }
                }
            } else {
                networkHelper.startMonitoring()
            }
        }
    }
}