package com.example.quoteit.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteit.DEFAULT_AUTHOR_NAME
import com.example.quoteit.DEFAULT_TAG
import com.example.quoteit.ERROR_MESSAGE
import com.example.quoteit.INTERNET_TURN_ON_REQUEST_MESSAGE
import com.example.quoteit.LOADING_MESSAGE
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.api.RetroFitInstance
import com.example.quoteit.data.Quote
import com.example.quoteit.data.TagsItem
import com.example.quoteit.db.tag.TagRepository
import com.example.quoteit.db.tag.TagEntity
import com.example.quoteit.utils.CacheImageHelper
import com.example.quoteit.utils.ContextHelper
import com.example.quoteit.utils.DateHelper
import com.example.quoteit.utils.GsonHelper
import com.example.quoteit.utils.NetworkHelper
import com.example.quoteit.utils.SharedPreferenceHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class HomeViewModel(contextHelper: ContextHelper,
                    val sharedPreferenceHelper: SharedPreferenceHelper,
                    val gsonHelper: GsonHelper<Quote>,
                    val tagRepository: TagRepository): ViewModel()  {

    val defaultErrorQuote =  Quote(
        "1",
        DEFAULT_AUTHOR_NAME,
        ERROR_MESSAGE,
        listOf(DEFAULT_TAG)
    )
    val defaultLoadingQuote =  Quote(
        "2",
        DEFAULT_AUTHOR_NAME,
        LOADING_MESSAGE,
        listOf(DEFAULT_TAG)
    )
    private val _uiState = MutableStateFlow<NetworkResponse<Quote>>(NetworkResponse.LoadingQuote(defaultLoadingQuote))
    val uiState: MutableStateFlow<NetworkResponse<Quote>> = _uiState
    val quoteService = RetroFitInstance.quoteService
    val todayDate: String = DateHelper.getDate()
    private val networkHelper: NetworkHelper = NetworkHelper(contextHelper){
        changeTodayQuote()
    }
    val tagsFlow = tagRepository.tagsFlow


    init{
        changeTodayQuote()
        fetchTagsAndStore()
    }

    fun changeTodayQuote() {
        if(sharedPreferenceHelper.contains(todayDate)) {
            val json = sharedPreferenceHelper.getValue(todayDate)
            val data = gsonHelper.getObj(json, Quote::class.java)
            _uiState.value = NetworkResponse.Success(data)
            Log.i("today",_uiState.value.toString())

        }
        else {
            if(networkHelper.isNetworkAvailable()) {
                networkHelper.stopMonitoring()
                viewModelScope.launch {
                    val response: Response<Quote> = quoteService.getRandomQuote()
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _uiState.value = NetworkResponse.Success(body)
                            val json = gsonHelper.getJson(body)
                            sharedPreferenceHelper.save(todayDate, json)
                        } else {
                            _uiState.value = NetworkResponse.ErrorQuote(defaultErrorQuote, response.message())
                        }
                    } else {
                        Log.e("error", response.message())
                        _uiState.value = NetworkResponse.ErrorQuote(defaultErrorQuote, response.message())
                    }
                }
            } else {
                _uiState.value = NetworkResponse.LoadingQuote(
                    defaultLoadingQuote.copy(content = INTERNET_TURN_ON_REQUEST_MESSAGE)
                )
                networkHelper.startMonitoring()
            }
        }
    }
    fun fetchTagsAndStore(){
        viewModelScope.launch {
            if(tagsFlow.value.isEmpty()){
                val response: Response<ArrayList<TagsItem>> = quoteService.getAllTags()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val filteredOutTags = CacheImageHelper.filterOutInvalidTags(body)
                        tagRepository.insertAllTag(filteredOutTags)
                    } else {
                        _uiState.value = NetworkResponse.Error("body is null")
                    }
                } else {
                    _uiState.value = NetworkResponse.Error(response.message())
                }

            }
        }
    }
    fun changeSelectedQuote(tag: String){
        viewModelScope.launch {
            if(networkHelper.isNetworkAvailable()) {
                networkHelper.stopMonitoring()
                viewModelScope.launch {
                    val response: Response<Quote> = quoteService.getRandomQuoteByTag(tag)
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _uiState.value = NetworkResponse.Success(body)
                        } else {
                            _uiState.value = NetworkResponse.ErrorQuote(defaultErrorQuote, response.message())
                        }
                    } else {
                        Log.e("error", response.message())
                        _uiState.value = NetworkResponse.ErrorQuote(defaultErrorQuote, response.message())
                    }
                }
            } else {
                _uiState.value = NetworkResponse.LoadingQuote(
                    defaultLoadingQuote.copy(content = INTERNET_TURN_ON_REQUEST_MESSAGE)
                )
                networkHelper.startMonitoring()
            }
        }
    }
}


