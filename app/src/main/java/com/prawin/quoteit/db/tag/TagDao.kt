package com.prawin.quoteit.db.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT * FROM tagentity WHERE isMarked=1")
    fun getMarkedTags(): Flow<List<TagEntity>>


    @Query("SELECT * FROM tagentity")
    suspend fun getAllTags(): List<TagEntity>

    @Query("SELECT * FROM tagentity")
    fun getAllTagsFlow(): Flow<List<TagEntity>>


    @Update
    suspend fun updateMark(tag: TagEntity)


    @Insert
    suspend fun insert(tag: TagEntity)
    @Insert
    suspend fun insertAll(tags: List<TagEntity>)

    @Query("DELETE FROM TagEntity")
    suspend fun deleteAll()
}