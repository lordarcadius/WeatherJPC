package com.vipuljha.weatherjpc.data.remote

import com.vipuljha.weatherjpc.utils.Constants.API_KEY
import com.vipuljha.weatherjpc.models.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v1/current.json")
    suspend fun getWeather(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String,
        @Query("aqi") aqi: String = "yes"
    ): Response<WeatherModel>
}