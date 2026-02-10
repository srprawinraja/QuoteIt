package com.prawin.quoteit.db.saved

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
interface SavedQuoteDao {
    @Insert
    suspend fun insert(vararg savedQuoteEntity: SavedQuoteEntity)

    @Query("SELECT * FROM SavedQuoteEntity")
    fun getAllSaved(): Flow<List<SavedQuoteEntity>>

    @Query("SELECT * FROM SavedQuoteEntity WHERE saved_quote_id=:id")
    suspend fun getSavedQuote(id: String): SavedQuoteEntity?

    @Query("DELETE FROM SavedQuoteEntity WHERE saved_quote_id=:id")
    suspend fun deleteQuote(id: String)
}