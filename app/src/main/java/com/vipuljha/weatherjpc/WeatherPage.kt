package com.vipuljha.weatherjpc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vipuljha.weatherjpc.models.WeatherModel
import com.vipuljha.weatherjpc.utils.NetworkResponse
import com.vipuljha.weatherjpc.utils.Utils
import com.vipuljha.weatherjpc.viewmodels.WeatherViewModel
import kotlin.math.roundToInt

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember {
        mutableStateOf("New Delhi")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val weatherState by viewModel.weather.collectAsState()

    LaunchedEffect(city) {
        viewModel.getWeather(city)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
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
                    modifier = Modifier.size(40.dp),
                    contentDescription = stringResource(R.string.search)
                )
            }
        }
        when (weatherState) {
            is NetworkResponse.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is NetworkResponse.Success -> {
                val weather = (weatherState as NetworkResponse.Success<WeatherModel>).data
                WeatherDetails(weather)
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

@Composable
fun WeatherDetails(data: WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${data.current.temp_c.roundToInt()}  °C",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Condition icon"
        )
        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    CurrentWeatherItems(
                        "AQI",
                        data.current.air_quality.pm2_5.roundToInt().toString()
                    )
                    CurrentWeatherItems("Humidity", "${data.current.humidity}%")
                    CurrentWeatherItems("Wind Speed", "${data.current.wind_kph.roundToInt()} km/h")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    CurrentWeatherItems("UV", Utils.getUVIndex(data.current.uv))
                    CurrentWeatherItems("Precipitation", "${data.current.precip_mm} mm")
                    CurrentWeatherItems("Pressure", "${data.current.pressure_mb} mb")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    CurrentWeatherItems("Visibility", "${data.current.vis_km} km")
                    CurrentWeatherItems("Time", data.location.localtime.split(" ")[1])
                    CurrentWeatherItems("Date", data.location.localtime.split(" ")[0])
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CurrentWeatherItems(title: String, data: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = data, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = title, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}