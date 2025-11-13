package com.example.quoteit.api

import com.example.quoteit.data.Quote
import com.example.quoteit.data.TagsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesService {
    @GET("/random")
    suspend fun getRandomQuote(): Response<Quote>
    @GET("/tags")
    suspend fun getAllTags(): Response<ArrayList<TagsItem>>

    @GET("/random")
    suspend fun getRandomQuoteByTag(@Query("tags") tag: String): Response<Quote>
}