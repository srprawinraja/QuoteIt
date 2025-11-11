package com.example.quoteit.data

import com.example.quoteit.R

data class TagsItem(
    val _id: String,
    val name: String,
    val slug: String,
    var img: Int = R.drawable.error_icon
)