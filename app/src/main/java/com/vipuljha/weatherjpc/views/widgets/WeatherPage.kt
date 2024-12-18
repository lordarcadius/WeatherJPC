package com.vipuljha.weatherjpc.views.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.vipuljha.weatherjpc.R
import com.vipuljha.weatherjpc.models.WeatherModel
import com.vipuljha.weatherjpc.utils.NetworkResponse
import com.vipuljha.weatherjpc.utils.Utils
import com.vipuljha.weatherjpc.viewmodels.WeatherViewModel
import kotlin.math.roundToInt

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("New Delhi") }
    val weatherState by viewModel.weather.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .windowInsetsPadding(
                WindowInsets
                    .statusBars
                    .only(WindowInsetsSides.Top)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CitySearchField(city = city, onCityChanged = {
            city = it
            viewModel.updateCity(city)
        })

        Spacer(Modifier.height(8.dp))

        when (weatherState) {
            is NetworkResponse.Loading -> LoadingIndicator()
            is NetworkResponse.Success -> WeatherDetails((weatherState as NetworkResponse.Success<WeatherModel>).data)
            is NetworkResponse.Error -> ErrorMessage((weatherState as NetworkResponse.Error).message)
        }
    }
}

@Composable
fun CitySearchField(
    city: String,
    onCityChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = city,
        onValueChange = onCityChanged,
        maxLines = 1,
        label = { Text(stringResource(R.string.search_city)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search)
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp)
    )
}


@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = "Error: $message",
        color = Color.Red,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
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
        LocationRow(data.location.name, data.location.country)

        Text(
            text = "${data.current.temp_c.roundToInt()} °C",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Condition icon",
            modifier = Modifier.size(160.dp)
        )

        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        WeatherDetailsCard(data)

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun LocationRow(name: String, country: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location Icon",
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(text = name, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.width(10.dp))
        Text(text = country, fontSize = 18.sp, color = Color.Gray)
    }
}

@Composable
fun WeatherDetailsCard(data: WeatherModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            WeatherDetailsRow(
                items = listOf(
                    "AQI" to data.current.air_quality.pm2_5.roundToInt().toString(),
                    "Humidity" to "${data.current.humidity}%",
                    "Wind Speed" to "${data.current.wind_kph.roundToInt()} km/h"
                )
            )
            WeatherDetailsRow(
                items = listOf(
                    "UV" to Utils.getUVIndex(data.current.uv),
                    "Precipitation" to "${data.current.precip_mm} mm",
                    "Pressure" to "${data.current.pressure_mb} mb"
                )
            )
            WeatherDetailsRow(
                items = listOf(
                    "Visibility" to "${data.current.vis_km} km",
                    "Time" to data.location.localtime.split(" ")[1],
                    "Date" to data.location.localtime.split(" ")[0]
                )
            )
        }
    }
}

@Composable
fun WeatherDetailsRow(items: List<Pair<String, String>>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items.forEach { (title, value) ->
            CurrentWeatherItems(title = title, data = value)
        }
    }
}

@Composable
fun CurrentWeatherItems(title: String, data: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = data, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}
