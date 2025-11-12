package com.example.quoteit.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quoteit.data.TagsItem
import com.example.quoteit.ui.data.UiTag

@Entity
data class TagEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "tag_name") val tagName: String,
    @ColumnInfo(name = "tag_slug") val tagSlug: String,
    @ColumnInfo(name = "tag_img") val tagImg: Int,
    @ColumnInfo(name = "tag_marked") val tagMarked: Boolean = false
)
fun TagEntity.migrateUi(): UiTag{
    return UiTag(
        id = id,
        name = tagName,
        slug = tagSlug,
        img = tagImg,
        marked = tagMarked
    )
}