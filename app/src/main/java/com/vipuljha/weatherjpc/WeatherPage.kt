package com.vipuljha.weatherjpc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vipuljha.weatherjpc.models.WeatherModel
import com.vipuljha.weatherjpc.utils.NetworkResponse
import com.vipuljha.weatherjpc.viewmodels.WeatherViewModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val weatherState by viewModel.weather.collectAsState()

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                maxLines = 1,
                value = city,
                onValueChange = {
                    city = it
                },
                label = {
                    Text(stringResource(R.string.search_city))
                }
            )
            Spacer(Modifier.width(20.dp))
            IconButton(onClick = {
                viewModel.getWeather(city)
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        }
        when (weatherState) {
            is NetworkResponse.Loading -> {
                Text("Loading...", modifier = Modifier.fillMaxWidth())
            }
            is NetworkResponse.Success -> {
                val weather = (weatherState as NetworkResponse.Success<WeatherModel>).data
                Text(
                    text = "Weather in ${weather.location.name}: ${weather.current.temp_c}°C",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is NetworkResponse.Error -> {
                val errorMessage = (weatherState as NetworkResponse.Error).message
                Text(
                    text = "Error: $errorMessage",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
