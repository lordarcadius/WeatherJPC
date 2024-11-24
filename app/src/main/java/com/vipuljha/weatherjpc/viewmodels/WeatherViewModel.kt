package com.vipuljha.weatherjpc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipuljha.weatherjpc.data.repositories.WeatherRepository
import com.vipuljha.weatherjpc.models.WeatherModel
import com.vipuljha.weatherjpc.utils.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weather =
        MutableStateFlow<NetworkResponse<WeatherModel>>(NetworkResponse.Loading)

    val weather: StateFlow<NetworkResponse<WeatherModel>> = _weather


    fun getWeather(keyword: String) = viewModelScope.launch {
        _weather.value = NetworkResponse.Loading
        try {
            val response = repository.getWeather(keyword)
            _weather.value = handleResponse(response)
        } catch (e: Exception) {
            _weather.value = NetworkResponse.Error(message = e.message ?: "Something went wrong!")
        }
    }

    private fun handleResponse(response: Response<WeatherModel>): NetworkResponse<WeatherModel> {
        if (response.isSuccessful) {
            response.body()?.let {
                return NetworkResponse.Success(it)
            } ?: return NetworkResponse.Error(message = "No data found")
        } else {
            return NetworkResponse.Error(message = response.message())
        }
    }
}