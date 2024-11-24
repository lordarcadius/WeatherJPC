package com.vipuljha.weatherjpc.utils

sealed class NetworkResponse<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResponse<T>(data)
    class Error<T>(data: T? = null, message: String?) : NetworkResponse<T>(data, message)
    class Loading<T>() : NetworkResponse<T>()
}