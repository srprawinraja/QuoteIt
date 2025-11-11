package com.example.quoteit.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteit.INTERNET_TURN_ON_REQUEST_MESSAGE
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.api.RetroFitInstance
import com.example.quoteit.data.Quote
import com.example.quoteit.data.TagsItem
import com.example.quoteit.utils.CacheImageHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class TagsViewModel (val cacheImageHelper: CacheImageHelper): ViewModel() {
    val quoteService = RetroFitInstance.quoteService
    private val _uiState = MutableStateFlow<NetworkResponse<List<TagsItem>>>(NetworkResponse.Loading)
    val uiState: MutableStateFlow<NetworkResponse<List<TagsItem>>> = _uiState
    fun getAllTags(){
        viewModelScope.launch {
            val response: Response<ArrayList<TagsItem>> = quoteService.getAllTags()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    _uiState.value = NetworkResponse.Success(cacheImageHelper.filterOutInvalidTags(body))
                } else {
                    _uiState.value = NetworkResponse.Error("body is null")
                }
            } else {
                _uiState.value = NetworkResponse.Error( response.message())
            }
        }
    }
}