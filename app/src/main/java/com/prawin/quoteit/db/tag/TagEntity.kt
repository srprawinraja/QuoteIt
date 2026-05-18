package com.prawin.quoteit.db.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // auto-increment
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "tag_name") val tagName: String,
)