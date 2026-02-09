package com.prawin.quoteit.viewModels

import android.util.Log
import android.widget.Toast
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

class TagsViewModel (val tagRepository: TagRepository, val contextHelper: ContextHelper): ViewModel() {
    private val _uiState = MutableStateFlow<NetworkResponse<List<TagsItem>>>(NetworkResponse.Loading)
    val uiState: MutableStateFlow<NetworkResponse<List<TagsItem>>> = _uiState
    private val networkHelper: NetworkHelper = NetworkHelper(contextHelper){
        getListOfTags()
    }
    val tagsFlow = tagRepository.tagsFlow
    val quoteService = RetroFitInstance.quoteService

    private val selectedTagsItem = mutableListOf<TagsItem>()

    init {
        getListOfTags()
    }

    fun getListOfTags(){
        viewModelScope.launch {
            try {
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
            }
        }
    }

    fun isTagMarked(tagId: String): Boolean{
        return tagsFlow.value.any { tag -> tag.tagId == tagId }
    }

    fun addSelectedTag(tag: TagsItem) {
        if(!isTagSelected(tag)){
            selectedTagsItem.add(tag)
        }
    }
    fun removeSelectedTag(tag: TagsItem) {
        if(isTagSelected(tag)){
            selectedTagsItem.remove(tag)
        }
    }

    fun isTagSelected(selectedTag: TagsItem): Boolean{
        for(tag in selectedTagsItem){
            if(tag.id == selectedTag.id){
                return true
            }
        }
        return false
    }

    fun commitChanges(){
        viewModelScope.launch {
            tagRepository.deleteAll()
            tagRepository.insertAll(selectedTagsItem)
            selectedTagsItem.clear()
            Toast.makeText(contextHelper.getContext(), "saved successfully", Toast.LENGTH_SHORT).show()
        }
    }


}