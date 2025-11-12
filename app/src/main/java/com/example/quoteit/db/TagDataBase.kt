package com.example.quoteit.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TagEntity::class], version = 1, exportSchema = false)
abstract class TagDataBase : RoomDatabase() {
    abstract fun tagDao(): TagDao
}