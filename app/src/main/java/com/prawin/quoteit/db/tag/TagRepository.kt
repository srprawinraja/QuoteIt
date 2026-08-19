package com.prawin.quoteit.db.tag

import android.content.Context
import com.prawin.quoteit.data.model.Tag
import com.prawin.quoteit.data.model.toTagEntity
import com.prawin.quoteit.db.QuoteDatabaseInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TagRepository(context: Context) {
    val db = QuoteDatabaseInstance.Companion.getInstance(context)
    val tagDao = db.tagDao()







    /*
     val tagsFlow = tagDao.getAllTagsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

     */



    fun getMarkedTags(): Flow<List<TagEntity>> =   tagDao.getMarkedTags()

    fun getAllTagsFlow(): Flow<List<TagEntity>> =   tagDao.getAllTagsFlow()

    suspend fun getAllTags() = tagDao.getAllTags()


    suspend fun updateMarked(tagEntity: List<TagEntity>){
        val tagsItems = tagDao.getAllTags()
        for(tag in tagsItems){
            for(changedTag in tagEntity){
                if(tag.id == changedTag.id){
                    tagDao.updateMark(changedTag)
                }
            }
        }
    }
    suspend fun insertAll(tagsItems : List<Tag>){
//        tagDao.deleteAll()
        val tagEntity = mutableListOf<TagEntity>()
        for (tagItem in tagsItems){
            tagEntity.add(tagItem.toTagEntity())
        }
        tagDao.insertAll(
                tagEntity
        )
    }
    suspend fun deleteAll(){
        tagDao.deleteAll()
    }
}