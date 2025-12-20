package com.example.quoteit.data

data class Quote(
    val id: String,
    val author: String,
    var quote: String,
    val tagId: String,
    val tag: String
)