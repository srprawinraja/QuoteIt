package com.example.quoteit.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.api.RetroFitInstance
import com.example.quoteit.data.TagsItem
import com.example.quoteit.db.TagDatabaseService
import com.example.quoteit.ui.data.UiTag
import com.example.quoteit.utils.CacheImageHelper
import com.example.quoteit.utils.TagDataClassFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class TagsViewModel (val tagDatabaseService: TagDatabaseService): ViewModel() {
    val quoteService = RetroFitInstance.quoteService
    private val _uiState = MutableStateFlow<NetworkResponse<List<UiTag>>>(NetworkResponse.Loading)
    val uiState: MutableStateFlow<NetworkResponse<List<UiTag>>> = _uiState
    init {
        getAllTags()
    }
    fun getAllTags(){
        viewModelScope.launch {
            if(!tagDatabaseService.isAnyRecordsExist()) {
                // add into db
                val response: Response<ArrayList<TagsItem>> = quoteService.getAllTags()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val filteredOutTags = CacheImageHelper.filterOutInvalidTags(body)
                        tagDatabaseService.insertTag(filteredOutTags)
                        _uiState.value = NetworkResponse.Success(TagDataClassFormatter.tagsApiToUi(filteredOutTags)) //
                    } else {
                        _uiState.value = NetworkResponse.Error("body is null")
                    }
                } else {
                    _uiState.value = NetworkResponse.Error( response.message())
                }
            } else {
                _uiState.value = NetworkResponse.Success(TagDataClassFormatter.tagsDbToUi(
                    tagDatabaseService.getAllTag()
                ))
            }
        }
    }
    fun changeMarked(id: String, marked: Boolean){
        viewModelScope.launch {
            tagDatabaseService.updateMarked(id, marked)
        }
    }
}