package com.vipuljha.weatherjpc.utils

object Utils {
    fun getUVIndex(uv: Double): String{
        return when {
            uv <= 2.0 -> "Low"
            uv <= 5.0 -> "Moderate"
            uv <= 7.0 -> "High"
            uv <= 10.0 -> "Very High"
            else -> "Extreme"
        }
    }
}