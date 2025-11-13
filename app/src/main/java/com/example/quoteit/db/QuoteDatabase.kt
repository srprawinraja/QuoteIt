package com.example.quoteit.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quoteit.db.tag.TagDao
import com.example.quoteit.db.tag.TagEntity

@Database(entities = [TagEntity::class], version = 1, exportSchema = false)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
}