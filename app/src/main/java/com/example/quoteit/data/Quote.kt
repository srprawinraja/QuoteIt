package com.example.quoteit.data

data class Quote(
    val id: String,
    val author: String,
    var quote: String,
    val slug: String,
    val tag: String
)