package com.example.smalltalk.api

import com.example.smalltalk.data.Quote
import retrofit2.http.GET

interface QuotesService {
    @GET("/random")
    suspend fun getRandomQuote(): Quote
}