package com.prawin.quoteit.db.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prawin.quoteit.data.model.Tag

@Entity
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // auto-increment
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "img") val img: String,
    @ColumnInfo(name = "tag_name") val tagName: String,
    @ColumnInfo(name = "isMarked") var isMarked: Boolean = false,
)


fun TagEntity.toTag() = Tag(
    documentId = id.toString(),
    img = img,
    slug = slug,
    tag = tagName
)


fun List<TagEntity>.toTags() = this.map { it.toTag() }
