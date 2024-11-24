package com.vipuljha.weatherjpc.data.remote

import com.vipuljha.weatherjpc.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.vipuljha.weatherjpc.BuildConfig

object RetrofitProvider {
    private fun createOkHttpClient(enableLogging: Boolean = BuildConfig.DEBUG): OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        if (enableLogging) {
            val logging = HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }

            client.addInterceptor(logging)
        }

        return client.build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(if (BASE_URL.endsWith("/")) BASE_URL else "$BASE_URL/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()
    }

    val api: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}