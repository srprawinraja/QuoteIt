package com.prawin.quoteit.data

import com.prawin.quoteit.db.tag.TagEntity

data class TagsItem(
    val id: String,
    val tag: String,
    val slug: String,
    val img: String,
)

fun TagsItem.toTagEntity() = TagEntity(
        tagId = this.slug,
        tagName =  this.tag,
    )


