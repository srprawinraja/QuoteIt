package com.example.quoteit.db.saved

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedQuoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // auto-increment
    @ColumnInfo(name = "saved_tag_name") val savedTagName: String,
    @ColumnInfo(name = "saved_quote") val savedQuote: String,
    @ColumnInfo(name = "saved_quote_id") val saveQuoteId: String,
    @ColumnInfo(name = "saved_quote_author") val savedAuthorQuote: String
)