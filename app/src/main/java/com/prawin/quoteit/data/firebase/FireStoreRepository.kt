package com.prawin.quoteit.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.prawin.quoteit.data.model.Quote
import com.prawin.quoteit.data.model.Tag
import com.prawin.quoteit.ui.screens.QuoteShow
import kotlinx.coroutines.tasks.await

object FireStoreRepository {
    private val db = FirebaseFirestore.getInstance()
    suspend fun getRandomQuoteBySlug(slug: String): Quote?{
        var result: QuerySnapshot = db.collection("quotes")
            .whereArrayContains("slugs", slug)
            .whereGreaterThanOrEqualTo("rand", Math.random())
            .orderBy("rand")
            .limit(1)
            .get().await()
        if(!result.isEmpty){
            return result.documents.firstOrNull()?.toObject(Quote::class.java)
        }
        result = db.collection("quotes")
            .whereArrayContains("slugs", slug)
            .whereLessThan("rand", Math.random())
            .orderBy("rand")
            .limit(1)
            .get().await()
        return result.documents.firstOrNull()?.toObject(Quote::class.java)
    }
    suspend fun getRandomQuote(): Quote?{
        val result: QuerySnapshot = db.collection("quotes")
            .whereGreaterThanOrEqualTo("rand", Math.random())
            .orderBy("rand")
            .limit(1)
            .get().await()
        return result.documents.firstOrNull()?.toObject(Quote::class.java)
    }

    suspend fun getTags(): List<Tag>{
        val result: QuerySnapshot = db.collection("tags")
            .get().await()
        return result.documents.mapNotNull { it -> it.toObject(Tag::class.java) }
    }


}