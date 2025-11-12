package com.example.quoteit.ui.data

import com.example.quoteit.db.TagEntity

data class UiTag(
    val id: String,
    val name: String,
    val slug: String,
    var img: Int,
    val marked: Boolean
)
