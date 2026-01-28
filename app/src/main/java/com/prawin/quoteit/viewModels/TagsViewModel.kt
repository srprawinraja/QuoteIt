package com.prawin.quoteit.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prawin.quoteit.api.NetworkResponse
import com.prawin.quoteit.api.RetroFitInstance
import com.prawin.quoteit.data.TagsItem
import com.prawin.quoteit.db.tag.TagEntity
import com.prawin.quoteit.db.tag.TagRepository
import com.prawin.quoteit.utils.ContextHelper
import com.prawin.quoteit.utils.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

private const val TAG = "TagsViewModel"

class TagsViewModel (val tagRepository: TagRepository, contextHelper: ContextHelper): ViewModel() {
    private val _uiState = MutableStateFlow<NetworkResponse<List<TagsItem>>>(NetworkResponse.Loading)
    val uiState: MutableStateFlow<NetworkResponse<List<TagsItem>>> = _uiState
    private val networkHelper: NetworkHelper = NetworkHelper(contextHelper){
        getListOfTags()
    }
    val tagsFlow = tagRepository.tagsFlow
    var allMarkedTags = listOf<TagEntity>()
    val quoteService = RetroFitInstance.quoteService

    init {
        getListOfTags()
    }

    fun getListOfTags(){
        viewModelScope.launch {
            try {
                getAllMarkedTag()
                if (networkHelper.isNetworkAvailable()) {
                    networkHelper.stopMonitoring()
                        val response: Response<List<TagsItem>> = quoteService.getAllTags()
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
                                _uiState.value = NetworkResponse.Success(body)
                            } else {
                                NetworkResponse.Error("no tags available")
                            }
                        } else {
                            NetworkResponse.Error("no tags available")
                        }

                } else {
                    NetworkResponse.Error("no internet")
                    networkHelper.startMonitoring()
                }
            } catch (e: Exception){
                NetworkResponse.Error("error occurred while retrieving tags")
               // Log.e(TAG, "failed to get list of tags", e)
            }
        }
    }

    fun getAllMarkedTag(){
        viewModelScope.launch {
           allMarkedTags =  tagRepository.getAllTags()
        }
    }
    fun isTagMarked(tagId: String): Boolean{
        return tagsFlow.value.any { tag -> tag.tagId == tagId }
    }
    fun unMarkTheTag(tagId: String){
        viewModelScope.launch {
            allMarkedTags.forEach { tag->
                if(tag.tagId==tagId){
                    tagRepository.delete(tag)
                }
            }
        }
    }
    fun markTheTag(tagsItem: TagsItem){
        viewModelScope.launch {
            tagRepository.insert(tagsItem)
        }
    }

}