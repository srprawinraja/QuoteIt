package com.example.quoteit.db.tag

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tagentity")
    fun getAllTagsFlow(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tagentity")
    suspend fun getAllTags(): List<TagEntity>
    @Insert
    suspend fun insert(vararg tag: TagEntity)

    @Query("UPDATE TagEntity SET tag_marked = :tagMarked WHERE id = :id")
    suspend fun updateMarked(id: Int, tagMarked: Boolean)

}