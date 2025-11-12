package com.example.quoteit.data

import com.example.quoteit.R
import com.example.quoteit.ui.data.UiTag

data class TagsItem(
    val _id: String,
    val name: String,
    val slug: String,
    var img: Int
)
fun TagsItem.migrateUi(): UiTag{
    return UiTag(
        id = _id,
        name = name,
        slug = slug,
        img = img,
        marked = false
    )
}
