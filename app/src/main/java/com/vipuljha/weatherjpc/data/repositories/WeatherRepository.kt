package com.vipuljha.weatherjpc.data.repositories

import com.vipuljha.weatherjpc.data.remote.RetrofitProvider
import com.vipuljha.weatherjpc.models.WeatherModel
import com.vipuljha.weatherjpc.utils.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WeatherRepository {
    private val api = RetrofitProvider.api

    fun getWeather(keyword: String): Flow<NetworkResponse<WeatherModel>> = flow {
        emit(NetworkResponse.Loading)
        try {
            val response = api.getWeather(query = keyword)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(NetworkResponse.Success(it))
                } ?: emit(NetworkResponse.Error("No data found"))
            } else {
                emit(NetworkResponse.Error(response.message()))
            }
        } catch (e: HttpException) {
            emit(NetworkResponse.Error("HTTP Error: ${e.message()}"))
        } catch (e: IOException) {
            emit(NetworkResponse.Error("Network Error: Please check your connection"))
        } catch (e: Exception) {
            emit(NetworkResponse.Error("Unexpected Error: ${e.message}"))
        }
    }
}
