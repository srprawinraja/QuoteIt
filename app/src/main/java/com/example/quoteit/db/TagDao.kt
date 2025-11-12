package com.example.quoteit.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import retrofit2.http.GET

@Dao
interface TagDao {
    @Query("SELECT * FROM tagentity")
    suspend fun getAll(): List<TagEntity>

    @Insert
    suspend fun insert(vararg tag: TagEntity)

    @Query("UPDATE TagEntity SET tag_marked = :tagMarked WHERE id = :id")
    suspend fun updateMarked(id: String, tagMarked: Boolean)

}