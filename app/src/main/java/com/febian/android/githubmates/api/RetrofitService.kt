package com.febian.android.githubmates.api

import com.febian.android.githubmates.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    private const val BASE_URL = "https://api.github.com/"

    private val retrofitService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
            Level.BODY else Level.NONE

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiInterface: ApiInterface by lazy {
        retrofitService
            .create(ApiInterface::class.java)
    }

}