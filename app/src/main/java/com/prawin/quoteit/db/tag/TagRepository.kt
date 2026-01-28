package com.prawin.quoteit.db.tag

import android.content.Context
import com.prawin.quoteit.data.TagsItem
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


    suspend fun insert(tagsItem : TagsItem){
            tagDao.insert(
                TagEntity(
                    tagId = tagsItem.slug,
                    tagName =  tagsItem.tag,
                )
            )
    }
    suspend fun delete(tagEntity: TagEntity){
        tagDao.delete(tagEntity)
    }
    suspend fun getAllTags() = tagDao.getAllTags()




}