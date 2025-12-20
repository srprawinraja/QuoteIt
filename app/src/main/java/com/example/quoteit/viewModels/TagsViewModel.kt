package com.example.quoteit.viewModels

import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.api.RetroFitInstance
import com.example.quoteit.api.RetroFitInstance.quoteService
import com.example.quoteit.data.ApiException
import com.example.quoteit.data.Quote
import com.example.quoteit.data.TagsItem
import com.example.quoteit.data.UpdateTagRequest
import com.example.quoteit.db.tag.TagEntity
import com.example.quoteit.db.tag.TagRepository
import com.example.quoteit.utils.CacheImageHelper
import com.example.quoteit.utils.ContextHelper
import com.example.quoteit.utils.NetworkHelper
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
    val quoteService = RetroFitInstance.quoteService

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
                Log.e(TAG, "failed to get list of tags", e)
            }
        }
    }

    fun updateTag(tagsItem: TagsItem){
        viewModelScope.launch {
            quoteService.updateTag(
                UpdateTagRequest(
                    tagsItem.id,
                    !tagsItem.marked
                )
            )
            if(!tagsItem.marked) {
                tagRepository.insert(tagsItem)
            }

        }
    }


}