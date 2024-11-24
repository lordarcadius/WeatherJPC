package com.vipuljha.weatherjpc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vipuljha.weatherjpc.data.repositories.WeatherRepository

@Suppress("UNCHECKED_CAST")
class WeatherViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WeatherViewModel::class.java) -> {
                WeatherViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}