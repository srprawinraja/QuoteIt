package com.prawin.quoteit.db.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tagentity")
    fun getAllTagsFlow(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tagentity")
    suspend fun getAllTags(): List<TagEntity>


    @Query("SELECT * FROM tagentity WHERE tag_id=:tagId")
    suspend fun getTag(tagId: String): TagEntity?

    @Insert
    suspend fun insert(tag: TagEntity)
    @Insert
    suspend fun insertAll(tags: List<TagEntity>)

    @Delete
    suspend fun delete(tag: TagEntity)

    @Query("DELETE FROM TagEntity")
    suspend fun deleteAll()
}