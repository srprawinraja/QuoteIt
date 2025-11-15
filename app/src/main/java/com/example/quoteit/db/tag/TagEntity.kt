package com.example.quoteit.db.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quoteit.R

@Entity
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // auto-increment
    @ColumnInfo(name = "tag_name") val tagName: String="",
    @ColumnInfo(name = "tag_slug") val tagSlug: String="",
    @ColumnInfo(name = "tag_img") val tagImg: Int= R.drawable.error_icon,
    @ColumnInfo(name = "tag_marked") val tagMarked: Boolean = false,
)