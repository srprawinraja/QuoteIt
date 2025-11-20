package com.example.quoteit.db.saved

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quoteit.db.tag.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedQuoteDao {
    @Insert
    suspend fun insert(vararg savedQuoteEntity: SavedQuoteEntity)

    @Query("SELECT * FROM SavedQuoteEntity")
    suspend fun getAllSaved(): List<SavedQuoteEntity>

    @Query("SELECT * FROM SavedQuoteEntity WHERE saved_quote_id=:quoteId")
    suspend fun isQuoteExist(quoteId: String): SavedQuoteEntity
}