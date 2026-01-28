package com.prawin.quoteit.db.saved

import android.content.Context
import com.prawin.quoteit.db.QuoteDatabaseInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SavedQuoteRepository (context: Context){
    private val TAG: String = "SavedQuoteRepository"
    val db = QuoteDatabaseInstance.Companion.getInstance(context)
    val savedQuoteDao = db.savedQuote()


    val savedQuoteFlow = savedQuoteDao.getAllSaved()
        .stateIn(
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
            started = SharingStarted.Companion.Eagerly,
            initialValue = emptyList())


    suspend fun saveQuote(savedQuoteEntity: SavedQuoteEntity){
        savedQuoteDao.insert(savedQuoteEntity)
    }

    suspend fun isQuoteExist(id: String): Boolean {
        return savedQuoteDao.getSavedQuote(id) != null
    }

}