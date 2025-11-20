package com.example.quoteit.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class SavedQuote (val id: Int, val tag: String, val quote: String, val quoteId: Int, val author: String)

