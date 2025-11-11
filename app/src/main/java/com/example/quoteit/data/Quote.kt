package com.example.quoteit.data

data class Quote(
    val _id: String,
    val author: String,
    var content: String,
    val tags: List<String>
)