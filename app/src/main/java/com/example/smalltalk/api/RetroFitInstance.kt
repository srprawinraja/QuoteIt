package com.example.smalltalk.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager


import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

const val BASE_URL = "https://api.quotable.io"

object RetroFitInstance {

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        // 1. Trust all certificates
        val trustAllCerts = arrayOf<X509TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        // 2. Create SSL context using the trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        // 3. Build OkHttpClient
        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0]) // Pass X509TrustManager explicitly
            .hostnameVerifier { _, _ -> true } // Trust all hostnames
            .build()
    }

    // 4. Build Retrofit instance
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getUnsafeOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: QuotesService = retrofit.create(QuotesService::class.java)
}
