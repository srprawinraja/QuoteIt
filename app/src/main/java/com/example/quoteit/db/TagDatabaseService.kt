package com.example.quoteit.db

import android.app.Application
import androidx.room.Room
import com.example.quoteit.data.TagsItem

class TagDatabaseService(application: Application) {
    val db = Room.databaseBuilder(
        application,
        TagDataBase::class.java, "tag-database"
    ).build()
    val tagDao = db.tagDao()

    suspend fun getAllTag(): List<TagEntity>{

        val tags: List<TagEntity> = tagDao.getAll()
        return tags
    }
    suspend fun insertTag(tagsItem : List<TagsItem>){

        tagsItem.forEach { tagsItem->
            tagDao.insert(
                TagEntity(
                    tagsItem._id,
                tagsItem.name,
                tagsItem.slug,
                tagsItem.img,
                )
            )
        }
    }
    suspend fun updateMarked(id: String, marked: Boolean){
        tagDao.updateMarked(id,marked)
    }
    suspend fun isAnyRecordsExist(): Boolean{
        return getAllTag().isNotEmpty()
    }

}
