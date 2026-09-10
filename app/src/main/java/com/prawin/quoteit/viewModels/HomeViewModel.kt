package com.prawin.quoteit.viewModels

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.type.DateTime
import com.prawin.quoteit.DEFAULT_AUTHOR_NAME
import com.prawin.quoteit.DEFAULT_TAG
import com.prawin.quoteit.ERROR_MESSAGE
import com.prawin.quoteit.INTERNET_TURN_ON_REQUEST_MESSAGE
import com.prawin.quoteit.LOADING_MESSAGE
import com.prawin.quoteit.api.NetworkResponse
import com.prawin.quoteit.data.firebase.FireStoreRepository
import com.prawin.quoteit.data.model.Quote
import com.prawin.quoteit.data.model.Streak
import com.prawin.quoteit.db.saved.SavedQuoteEntity
import com.prawin.quoteit.db.saved.SavedQuoteRepository
import com.prawin.quoteit.db.tag.TagEntity
import com.prawin.quoteit.db.tag.TagRepository
import com.prawin.quoteit.utils.ContextHelper
import com.prawin.quoteit.utils.DateHelper
import com.prawin.quoteit.utils.GsonHelper
import com.prawin.quoteit.utils.NetworkHelper
import com.prawin.quoteit.utils.SharedPreferenceHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

private const val TAG = "HomeViewModel"
class HomeViewModel(
    val contextHelper: ContextHelper,
    val sharedPreferenceHelper: SharedPreferenceHelper,
    val gsonHelper: GsonHelper<Quote>,
    val tagRepository: TagRepository,
    val savedQuoteRepository: SavedQuoteRepository
): ViewModel()  {

    val defaultErrorQuote =  Quote(
        "1",
        DEFAULT_AUTHOR_NAME,
        ERROR_MESSAGE,
        0.0,
        listOf(DEFAULT_TAG),
        DEFAULT_TAG
    )
    val defaultLoadingQuote =  Quote(
        "2",
        DEFAULT_AUTHOR_NAME,
        LOADING_MESSAGE,
        0.0,
        listOf(DEFAULT_TAG),
        DEFAULT_TAG
    )


    val markedTagsFlow = tagRepository.getMarkedTags()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow<NetworkResponse<Quote>>(NetworkResponse.LoadingQuote(defaultLoadingQuote))
    val uiState: MutableStateFlow<NetworkResponse<Quote>> = _uiState
    private val _streak = MutableStateFlow(1)
    val streak = _streak.asStateFlow()
    val todayDate: String = DateHelper.getDate()

    private val networkHelper: NetworkHelper = NetworkHelper(contextHelper){
        updateTodayQuote()

    }
    var selectedId by mutableIntStateOf(0)
    private var _marked = mutableStateOf(false)
    val marked = _marked

    init{
        updateTodayQuote()
       // loadQuotes()
    }

    fun updateMarked(id: String){
        viewModelScope.launch {
            _marked.value = savedQuoteRepository.isQuoteExist(id)
        }
    }
    fun changeMarked(marked: Boolean){
        _marked.value = marked
    }

    fun updateTodayQuote() {
        _uiState.value = NetworkResponse.LoadingQuote(
            defaultLoadingQuote.copy(quote = LOADING_MESSAGE)
        )
        val json = sharedPreferenceHelper.getValue(todayDate)
        if(json != null){
            val data:Quote = gsonHelper.getObj(json, Quote::class.java)
            updateMarked(data.documentId)
            _uiState.value = NetworkResponse.Success(data)
        } else {
            if(networkHelper.isNetworkAvailable()) {
                networkHelper.stopMonitoring()
                viewModelScope.launch {
                    try {
                        val quotes: List<Quote> = FireStoreRepository.getNRandomQuotes(2)

                        if (quotes.isNotEmpty()) {
                            val todayQuote = quotes[0]
                            val tomorrowQuote = quotes[1]
                             updateMarked(todayQuote.documentId)
                            _uiState.value = NetworkResponse.Success(todayQuote)
                            sharedPreferenceHelper.save(todayDate, gsonHelper.getJson(todayQuote))
                            sharedPreferenceHelper.save(todayDate, gsonHelper.getJson(tomorrowQuote))
                        } else {
                            _uiState.value =
                                NetworkResponse.ErrorQuote(defaultErrorQuote, "failed to update today quote")
                        }
                    } catch (exception: Exception){
                        uiState.value =
                            NetworkResponse.ErrorQuote(defaultErrorQuote, exception.message.toString())
                        //Log.e(TAG, exception.toString());
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

    fun updateSelectedTagQuote(slug: String){
        _uiState.value = NetworkResponse.LoadingQuote(
            defaultLoadingQuote.copy(quote = LOADING_MESSAGE)
        )
        viewModelScope.launch {
            try {
                if (networkHelper.isNetworkAvailable()) {
                    networkHelper.stopMonitoring()
                    viewModelScope.launch {
                        try {
                            val quote: Quote? = FireStoreRepository.getRandomQuoteBySlug(slug)
                            if (quote != null) {
                                updateMarked(quote.documentId)
                                _uiState.value = NetworkResponse.Success(quote)

                            } else {

                                //Log.i(TAG, "failed to update selected tag quote",



                                _uiState.value =
                                    NetworkResponse.ErrorQuote(
                                        defaultErrorQuote,
                                        "failed to update selected tag quote"
                                    )
                            }
                        } catch (exception: Exception){
                            NetworkResponse.ErrorQuote(defaultErrorQuote, exception.message.toString())
                        }
                    }
                } else {

                    _uiState.value = NetworkResponse.LoadingQuote(
                        defaultLoadingQuote.copy(quote = INTERNET_TURN_ON_REQUEST_MESSAGE)
                    )
                    networkHelper.startMonitoring()
                }
            } catch (exception: Exception){
                NetworkResponse.ErrorQuote(defaultErrorQuote, exception.message.toString())

                //Log.e(TAG, "failed to update selected tag quote", exception);
            }
        }
    }
    fun updateTag(tagEntity: TagEntity){
        viewModelScope.launch {
            tagEntity.isMarked = !tagEntity.isMarked
            tagRepository.updateMarked(listOf(tagEntity))
            selectedId = 0
            updateTodayQuote()
        }
    }
    fun deleteQuote(id: String){
        viewModelScope.launch {
            savedQuoteRepository.deleteQuote(id)
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
               //Log.i(TAG, "saving the quote...")
             if(_uiState.value is NetworkResponse.Success  && !savedQuoteRepository.isQuoteExist(id)) {
                    savedQuoteRepository.saveQuote(
                        SavedQuoteEntity(
                            savedQuote = quote,
                            saveQuoteId = id,
                            savedAuthor = author,
                            savedTagName = tag,
                        )
                    )
                 changeMarked(true)

             }
            }  catch (exception: Exception){
                NetworkResponse.ErrorQuote(defaultErrorQuote, exception.message.toString())
                //Log.e(TAG, "failed to save the quote", exception)
            }
        }
    }
     fun getStreak(uId: String){
        viewModelScope.launch {
                val todayDate = LocalDate.now()
                val key = "$todayDate streak"
                val result = FireStoreRepository.getStreak(uId)
                val streak = sharedPreferenceHelper.getValue(key = key)
                if (streak!=null) {
                    _streak.value = streak.toInt()
                } else if (result == null || result.lastCompletedDate != todayDate.minusDays(1)
                        .toString()
                ) {
                    FireStoreRepository.updateStreak(uId, Streak(1, todayDate.toString()))
                    sharedPreferenceHelper.save(key, "1")
                } else {
                    FireStoreRepository.updateStreak(
                        uId,
                        Streak(result.currentStreak + 1, LocalDate.now().toString())
                    )
                    sharedPreferenceHelper.save(key, (result.currentStreak + 1).toString())
                    _streak.value = result.currentStreak + 1
                }

        }
    }

}





