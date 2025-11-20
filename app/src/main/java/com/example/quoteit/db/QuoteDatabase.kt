package com.example.quoteit.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quoteit.db.saved.SavedQuoteDao
import com.example.quoteit.db.saved.SavedQuoteEntity
import com.example.quoteit.db.tag.TagDao
import com.example.quoteit.db.tag.TagEntity

@Database(entities = [TagEntity::class, SavedQuoteEntity::class], version = 1, exportSchema = false)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
    abstract fun savedQuote(): SavedQuoteDao

}