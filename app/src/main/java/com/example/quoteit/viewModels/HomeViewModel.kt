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
import com.example.quoteit.db.saved.SavedQuoteEntity
import com.example.quoteit.db.saved.SavedQuoteRepository
import com.example.quoteit.db.tag.TagRepository
import com.example.quoteit.utils.ContextHelper
import com.example.quoteit.utils.DateHelper
import com.example.quoteit.utils.GsonHelper
import com.example.quoteit.utils.NetworkHelper
import com.example.quoteit.utils.SharedPreferenceHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber


class HomeViewModel(
    contextHelper: ContextHelper,
    val sharedPreferenceHelper: SharedPreferenceHelper,
    val gsonHelper: GsonHelper<Quote>,
    val tagRepository: TagRepository,
    val savedQuoteRepository: SavedQuoteRepository
): ViewModel()  {

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
        updateTodayQuote()
    }
    val tagsFlow = tagRepository.tagsFlow


    init{
        updateTodayQuote()
    }

    fun updateTodayQuote() {
        if(sharedPreferenceHelper.contains(todayDate)) {
            val json = sharedPreferenceHelper.getValue(todayDate)
            val data = gsonHelper.getObj(json, Quote::class.java)
            _uiState.value = NetworkResponse.Success(data)
        }
        else {
            if(networkHelper.isNetworkAvailable()) {
                networkHelper.stopMonitoring()
                viewModelScope.launch {
                    try {
                        val response: Response<Quote> = quoteService.getRandomQuote()
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
                                _uiState.value = NetworkResponse.Success(body)
                                val json = gsonHelper.getJson(body)
                                sharedPreferenceHelper.save(todayDate, json)
                            } else {
                                _uiState.value = NetworkResponse.ErrorQuote(
                                    defaultErrorQuote,
                                    response.message()
                                )
                            }
                        } else {
                            Timber.i( "request unsuccessful. Failed to update today quote");
                            _uiState.value =
                                NetworkResponse.ErrorQuote(defaultErrorQuote, response.message())
                        }
                    } catch (exception: Exception){
                        Timber.e(exception, "Failed to update today quote");
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

    fun updateSelectedTagQuote(tag: String){
        viewModelScope.launch {
            try {
                if (networkHelper.isNetworkAvailable()) {
                    networkHelper.stopMonitoring()
                    viewModelScope.launch {
                        val response: Response<Quote> = quoteService.getRandomQuoteByTag(tag)
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
                                _uiState.value = NetworkResponse.Success(body)

                            } else {
                                _uiState.value = NetworkResponse.ErrorQuote(
                                    defaultErrorQuote,
                                    response.message()
                                )
                            }
                        } else {
                            Timber.i( "request unsuccessful. Failed to update selected tag quote");
                            _uiState.value =
                                NetworkResponse.ErrorQuote(defaultErrorQuote, response.message())
                        }
                    }
                } else {
                    _uiState.value = NetworkResponse.LoadingQuote(
                        defaultLoadingQuote.copy(content = INTERNET_TURN_ON_REQUEST_MESSAGE)
                    )
                    networkHelper.startMonitoring()
                }
            } catch (exception: Exception){
                Timber.e(exception, "Failed to update selected tag quote");
            }
        }
    }
    fun changeTagMark(id: Int, marked: Boolean){
        viewModelScope.launch {
            try {
                tagRepository.updateMarked(id, marked)
            } catch (exception: Exception){
                Timber.e(exception, "Failed to change quote mark");
            }
        }
    }
    fun saveQuote(
        id: String,
        quote: String,
        author: String,
        tag: String
    ){
        viewModelScope.launch {
            try {
                savedQuoteRepository.saveQuote(
                    SavedQuoteEntity(
                        savedQuote = quote,
                        saveQuoteId = id,
                        savedAuthorQuote = author,
                        savedTagName = tag,
                    )
                )
            } catch (exception: Exception){
                Timber.e(exception, "failed to save the quote");
            }
        }
    }
    fun isQuoteExist(quoteId: Int){
        viewModelScope.launch {
            savedQuoteRepository.isQuoteExist(quoteId)
        }
    }

}


