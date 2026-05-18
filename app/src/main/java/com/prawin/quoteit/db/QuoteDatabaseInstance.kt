package com.prawin.quoteit.db

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class QuoteDatabaseInstance {
    companion object {
        @Volatile
        private var INSTANCE: QuoteDatabase? = null
        val migration1To2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    "ALTER TABLE tagentity RENAME COLUMN tag_id TO slug"
                )
            }
        }
        fun getInstance(context: Context): QuoteDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java,
                    "app_db"
                ).addMigrations(migration1To2).build().also { INSTANCE = it }
            }
        }
    }
}