package com.example.quoteit.db.tag

import android.content.Context
import com.example.quoteit.R
import com.example.quoteit.data.TagsItem
import com.example.quoteit.db.QuoteDatabaseInstance
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

    suspend fun getAllTag(): List<TagEntity>{
        return tagDao.getAllTags()
    }

    suspend fun insertAllTag(tagsItem : List<TagsItem>){
        tagsItem.forEach { tagsItem->
            tagDao.insert(
                TagEntity(
                    tagName =  tagsItem.name,
                    tagSlug = tagsItem.slug,
                    tagImg = tagsItem.img,
                )
            )
        }
    }


    suspend fun updateMarked(id: Int, marked: Boolean){
        tagDao.updateMarked(id,marked)
    }
}