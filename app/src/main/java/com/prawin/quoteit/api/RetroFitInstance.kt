package com.prawin.quoteit.api

import com.prawin.quoteit.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit




const val BASE_URL = "http://quoteit-env-1.eba-mecyfpmd.ap-south-1.elasticbeanstalk.com/"

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("x-api-key", apiKey)
            .build()
        return chain.proceed(request)
    }
}


object RetroFitInstance {


    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor(BuildConfig.API_KEY)
        )
        .build();

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val quoteService: QuotesService = retrofit.create(QuotesService::class.java)
}


