package com.example.quoteit.db.saved

import android.content.Context
import androidx.room.Query
import com.example.quoteit.data.SavedQuote
import com.example.quoteit.db.QuoteDatabaseInstance

class SavedQuoteRepository (context: Context){
    val db = QuoteDatabaseInstance.Companion.getInstance(context)
    val savedQuoteDao = db.savedQuote()

    suspend fun saveQuote(savedQuoteEntity: SavedQuoteEntity){
        savedQuoteDao.insert(savedQuoteEntity)
    }
    suspend fun getAllSavedQuote(): List<SavedQuoteEntity> {
        return savedQuoteDao.getAllSaved()
    }
    suspend fun isQuoteExist(id: Int): Boolean {
        return savedQuoteDao.getSavedQuote(id) == null
    }
}