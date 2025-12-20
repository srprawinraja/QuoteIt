package com.example.quoteit.db.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.quoteit.db.saved.SavedQuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tagentity")
    fun getAllTagsFlow(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tagentity")
    suspend fun getAllTags(): List<TagEntity>


    @Query("SELECT * FROM tagentity WHERE tag_id=:id")
    suspend fun getTag(id: String): TagEntity?

    @Insert
    suspend fun insert(vararg tag: TagEntity)

    @Delete
    suspend fun delete(vararg tag: TagEntity)
}