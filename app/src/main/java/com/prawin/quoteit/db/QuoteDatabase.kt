package com.prawin.quoteit.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.prawin.quoteit.db.saved.SavedQuoteDao
import com.prawin.quoteit.db.saved.SavedQuoteEntity
import com.prawin.quoteit.db.tag.TagDao
import com.prawin.quoteit.db.tag.TagEntity

@Database(entities = [TagEntity::class, SavedQuoteEntity::class], version = 1, exportSchema = false)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
    abstract fun savedQuote(): SavedQuoteDao

}