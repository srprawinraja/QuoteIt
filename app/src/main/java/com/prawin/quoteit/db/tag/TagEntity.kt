package com.prawin.quoteit.db.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // auto-increment
    @ColumnInfo(name = "tag_id") val tagId: String,
    @ColumnInfo(name = "tag_name") val tagName: String,
)