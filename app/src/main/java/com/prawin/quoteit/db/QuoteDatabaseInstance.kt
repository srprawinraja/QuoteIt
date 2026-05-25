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
        val migration2To3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    "ALTER TABLE tagentity ADD COLUMN img TEXT NOT NULL DEFAULT ''"
                )

                db.execSQL(
                    "ALTER TABLE tagentity ADD COLUMN isMarked INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
        fun getInstance(context: Context): QuoteDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java,
                    "app_db"
                ).addMigrations(migration1To2)
                    .addMigrations(migration2To3).build().also { INSTANCE = it }
            }
        }
    }
}