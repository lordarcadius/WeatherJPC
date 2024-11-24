package com.vipuljha.weatherjpc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipuljha.weatherjpc.data.repositories.WeatherRepository
import com.vipuljha.weatherjpc.models.WeatherModel
import com.vipuljha.weatherjpc.utils.NetworkResponse
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weather =
        MutableStateFlow<NetworkResponse<WeatherModel>>(NetworkResponse.Loading)
    val weather: StateFlow<NetworkResponse<WeatherModel>> = _weather

    private val _city = MutableStateFlow("New Delhi")

    init {
        // Fetch data for the default city on app launch
        fetchWeather("New Delhi")

        // Debounced API call for user interactions
        _city
            .debounce(500) // Wait 500ms after user stops typing
            .distinctUntilChanged() // Avoid duplicate API calls
            .filter { it != "New Delhi" } // Avoid triggering for initial default city
            .onEach { city ->
                fetchWeather(city)
            }
            .launchIn(viewModelScope)
    }

    fun updateCity(city: String) {
        _city.value = city
    }

    private fun fetchWeather(city: String) {
        viewModelScope.launch {
            repository.getWeather(city).collect { response ->
                _weather.value = response
            }
        }
    }
}
