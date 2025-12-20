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
import com.example.quoteit.data.ApiException
import com.example.quoteit.data.Quote
import com.example.quoteit.db.saved.SavedQuoteEntity
import com.example.quoteit.db.saved.SavedQuoteRepository
import com.example.quoteit.db.tag.TagEntity
import com.example.quoteit.db.tag.TagRepository
import com.example.quoteit.utils.ContextHelper
import com.example.quoteit.utils.DateHelper
import com.example.quoteit.utils.GsonHelper
import com.example.quoteit.utils.NetworkHelper
import com.example.quoteit.utils.SharedPreferenceHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

private const val TAG = "HomeViewModel"
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
        "1",
        DEFAULT_TAG
    )
    val defaultLoadingQuote =  Quote(
        "2",
        DEFAULT_AUTHOR_NAME,
        LOADING_MESSAGE,
        "2",
        DEFAULT_TAG
    )
    private val _uiState = MutableStateFlow<NetworkResponse<Quote>>(NetworkResponse.LoadingQuote(defaultLoadingQuote))
    val uiState: MutableStateFlow<NetworkResponse<Quote>> = _uiState
    val quoteService = RetroFitInstance.quoteService
    val todayDate: String = DateHelper.getDate()
    private val networkHelper: NetworkHelper = NetworkHelper(contextHelper){
        updateTodayQuote()
    }
    val tagsFlow = tagRepository.tagsFlow
    private var quoteRequestResponse = false;

    init{
        updateTodayQuote()
    }

    fun updateTodayQuote() {
        if(sharedPreferenceHelper.contains(todayDate)) {
            quoteRequestResponse = true
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
                                quoteRequestResponse = true
                                _uiState.value = NetworkResponse.Success(body)
                                val json = gsonHelper.getJson(body)
                                sharedPreferenceHelper.save(todayDate, json)
                            } else {
                                quoteRequestResponse = false
                                _uiState.value = NetworkResponse.ErrorQuote(
                                    defaultErrorQuote,
                                    response.message()
                                )
                            }
                        } else {
                            quoteRequestResponse = false
                            _uiState.value =
                                NetworkResponse.ErrorQuote(defaultErrorQuote, response.message())
                            Log.i(TAG, "failed to update today quote", ApiException(response.code().toString()+" "+response.message()))

                        }
                    } catch (exception: Exception){
                        quoteRequestResponse = false
                        Log.e(TAG, exception.toString());
                    }
                }
            } else {
                _uiState.value = NetworkResponse.LoadingQuote(
                    defaultLoadingQuote.copy(quote = INTERNET_TURN_ON_REQUEST_MESSAGE)
                )
                networkHelper.startMonitoring()
            }
        }
    }

    fun updateSelectedTagQuote(tagId: String){
        viewModelScope.launch {
            try {
                if (networkHelper.isNetworkAvailable()) {
                    networkHelper.stopMonitoring()
                    viewModelScope.launch {
                        val response: Response<Quote> = quoteService.getRandomQuoteByTag(tagId)
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
                                quoteRequestResponse = true
                                _uiState.value = NetworkResponse.Success(body)

                            } else {
                                quoteRequestResponse = false

                                _uiState.value = NetworkResponse.ErrorQuote(
                                    defaultErrorQuote,
                                    response.message()
                                )
                            }
                        } else {
                            quoteRequestResponse = false

                            Log.i(TAG, "failed to update selected tag quote", ApiException(response.code().toString()+" "+response.message()))

                            _uiState.value =
                                NetworkResponse.ErrorQuote(defaultErrorQuote, response.message())
                        }
                    }
                } else {
                    quoteRequestResponse = false

                    _uiState.value = NetworkResponse.LoadingQuote(
                        defaultLoadingQuote.copy(quote = INTERNET_TURN_ON_REQUEST_MESSAGE)
                    )
                    networkHelper.startMonitoring()
                }
            } catch (exception: Exception){
                quoteRequestResponse = false
                Log.e(TAG, "failed to update selected tag quote", exception);
            }
        }
    }
    fun deleteTag(tagEntity: TagEntity){
        viewModelScope.launch {
            tagRepository.delete(tagEntity)
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
                if(_uiState.value is NetworkResponse.Success  && !savedQuoteRepository.isQuoteExist(id)) {
                    savedQuoteRepository.saveQuote(
                        SavedQuoteEntity(
                            savedQuote = quote,
                            saveQuoteId = id,
                            savedAuthor = author,
                            savedTagName = tag,
                        )
                    )
                }
            } catch (exception: Exception){
                Log.e(TAG, "failed to save the quote", exception);

            }
        }
    }
}


