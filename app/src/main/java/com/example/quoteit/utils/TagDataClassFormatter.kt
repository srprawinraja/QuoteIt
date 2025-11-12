package com.example.quoteit.utils

import com.example.quoteit.data.TagsItem
import com.example.quoteit.db.TagDao
import com.example.quoteit.db.TagEntity
import com.example.quoteit.ui.data.UiTag

object TagDataClassFormatter {
    fun tagsApiToUi(tags: List<TagsItem>): List<UiTag>{
        return tags.map { tag ->
            UiTag(
                id=tag._id,
                name=tag.name,
                slug = tag.slug,
                img = tag.img,
                marked = false
            )

        }
    }
    fun tagsDbToUi(tagEntity: List<TagEntity>): List<UiTag>{
        return tagEntity.map{ tagsEntity ->
            UiTag(
                id=tagsEntity.id,
                name=tagsEntity.tagName,
                slug = tagsEntity.tagSlug,
                img = tagsEntity.tagImg,
                marked = tagsEntity.tagMarked
            )
        }
    }
}
//