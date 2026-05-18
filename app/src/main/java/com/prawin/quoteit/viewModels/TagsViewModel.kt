package com.prawin.quoteit.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prawin.quoteit.api.NetworkResponse
import com.prawin.quoteit.data.firebase.FireStoreRepository
import com.prawin.quoteit.data.model.Tag
import com.prawin.quoteit.db.tag.TagEntity
import com.prawin.quoteit.db.tag.TagRepository
import com.prawin.quoteit.utils.ContextHelper
import com.prawin.quoteit.utils.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

private const val TAG = "TagsViewModel"

class TagsViewModel (val tagRepository: TagRepository, val contextHelper: ContextHelper): ViewModel() {
    private val _uiState = MutableStateFlow<NetworkResponse<List<Tag>>>(NetworkResponse.Loading)
    val uiState: MutableStateFlow<NetworkResponse<List<Tag>>> = _uiState
    private val networkHelper: NetworkHelper = NetworkHelper(contextHelper){
        getListOfTags()
    }
    val tagsFlow = tagRepository.tagsFlow

    private val selectedTagsItem = mutableListOf<Tag>()

    init {
        getListOfTags()
    }

    fun getListOfTags(){
        viewModelScope.launch {
            try {
                if (networkHelper.isNetworkAvailable()) {
                    networkHelper.stopMonitoring()
                        val tags: List<Tag> = FireStoreRepository.getTags()

                       // Log.d(TAG, tags.toString())
                        if(tags.isNotEmpty()){
                                _uiState.value = NetworkResponse.Success(tags)
                        } else {
                            NetworkResponse.Error("no tags available")
                        }
                }
                 else {
                    NetworkResponse.Error("no internet")
                    networkHelper.startMonitoring()
                }
            } catch (e: Exception){
               // Log.d(TAG, e.toString())
                NetworkResponse.Error("error occurred while retrieving tags")
            }
        }
    }

    fun isTagMarked(slug: String): Boolean{
        return tagsFlow.value.any { tag -> tag.slug == slug }
    }

    fun addSelectedTag(tag: Tag) {
        //
        // Log.d(TAG+"ADD", tag.toString())
        if(!isTagSelected(tag)){
            selectedTagsItem.add(tag)
        }
    }
    fun removeSelectedTag(tag: Tag) {
        if(isTagSelected(tag)){
            selectedTagsItem.remove(tag)
        }
    }

    fun isTagSelected(selectedTag: Tag): Boolean{
        for(tag in selectedTagsItem){
            if(tag.documentId == selectedTag.documentId){
                return true
            }
        }
        return false
    }

    fun commitChanges(){
        viewModelScope.launch {
//            tagRepository.deleteAll()
            tagRepository.insertAll(selectedTagsItem)
            selectedTagsItem.clear()
            Toast.makeText(contextHelper.getContext(), "saved successfully", Toast.LENGTH_SHORT).show()
        }
    }


}