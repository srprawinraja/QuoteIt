package com.prawin.quoteit.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.prawin.quoteit.db.tag.TagEntity

data class Tag (
    @DocumentId
    val documentId: String="",
    val img: String="",
    val slug:String="",
    val tag:String=""
)

fun Tag.toTagEntity() = TagEntity(
    slug = slug,
    img =  img,
    tagName = tag,
)

