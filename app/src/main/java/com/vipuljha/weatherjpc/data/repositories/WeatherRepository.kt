package com.vipuljha.weatherjpc.data.repositories

import com.vipuljha.weatherjpc.data.remote.RetrofitProvider

class WeatherRepository {
    private val api = RetrofitProvider.api

    suspend fun getWeather(keyword: String) = api.getWeather(query = keyword)
}