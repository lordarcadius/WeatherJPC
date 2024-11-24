package com.vipuljha.weatherjpc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vipuljha.weatherjpc.ui.theme.WeatherJPCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherJPCTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .windowInsetsPadding(
                            WindowInsets
                                .statusBars
                                .only(WindowInsetsSides.Top)
                        ),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    WeatherPage()
                }
            }
        }
    }
}
