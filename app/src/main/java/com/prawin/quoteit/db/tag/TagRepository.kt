package com.prawin.quoteit.db.tag

import android.content.Context
import com.prawin.quoteit.data.TagsItem
import com.prawin.quoteit.data.toTagEntity
import com.prawin.quoteit.db.QuoteDatabaseInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class TagRepository(context: Context) {
    val db = QuoteDatabaseInstance.Companion.getInstance(context)
    val tagDao = db.tagDao()

    val tagsFlow = tagDao.getAllTagsFlow()
        .stateIn(
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
            started = SharingStarted.Companion.Eagerly,
            initialValue = emptyList())


    suspend fun insertAll(tagsItems : List<TagsItem>){
        tagDao.deleteAll()
        val tagEntity = mutableListOf<TagEntity>()
        for (tagItem in tagsItems){
            tagEntity.add(tagItem.toTagEntity())
        }
        tagDao.insertAll(
                tagEntity
        )
    }
    suspend fun delete(tagEntity: TagEntity){
        tagDao.delete(tagEntity)
    }
    suspend fun deleteAll(){
        tagDao.deleteAll()
    }





}