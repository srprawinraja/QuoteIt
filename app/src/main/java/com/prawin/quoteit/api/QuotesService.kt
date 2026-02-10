package com.prawin.quoteit.api

import com.prawin.quoteit.data.Quote
import com.prawin.quoteit.data.TagsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface QuotesService {
    @GET("random")
    suspend fun getRandomQuote(): Response<Quote>
    @GET("tags")
    suspend fun getAllTags(): Response<List<TagsItem>>

    @GET("random")
    suspend fun getRandomQuoteByTag(@Query("slug") slug: String): Response<Quote>

    @POST("secure-endpoint")
    suspend fun verifyToken(@Header("x-firebase-appCheck") token: String)

}