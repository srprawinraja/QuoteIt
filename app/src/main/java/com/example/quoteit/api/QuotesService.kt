package com.example.quoteit.api

import com.example.quoteit.data.Quote
import com.example.quoteit.data.TagsItem
import retrofit2.Response
import retrofit2.http.GET

interface QuotesService {
    @GET("/random")
    suspend fun getRandomQuote(): Response<Quote>
    @GET("/tags")
    suspend fun getAllTags(): Response<ArrayList<TagsItem>>
}