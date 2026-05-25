package com.prawin.quoteit.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Quote(
    @DocumentId
    val documentId: String = "",
    val author: String = "",
    val quote: String = "",
    val rand: Double = 0.0,
    val slugs: List<String> = emptyList(),
    val tagName: String = ""
)