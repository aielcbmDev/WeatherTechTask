package com.charly.weatherapp.ui.detail.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.charly.weatherapp.ui.common.display.DisplayDataHorizontally
import com.charly.weatherapp.ui.common.display.DisplayDataVertically
import com.charly.weatherapp.ui.detail.model.DailyForecastDetailModel
import org.jetbrains.compose.resources.stringResource
import weathertechtask.feature_weather.weatherapp.generated.resources.Res
import weathertechtask.feature_weather.weatherapp.generated.resources.detail_screen_maximum_temperature_title
import weathertechtask.feature_weather.weatherapp.generated.resources.detail_screen_minimum_temperature_title
import weathertechtask.feature_weather.weatherapp.generated.resources.detail_screen_summary_title
import weathertechtask.feature_weather.weatherapp.generated.resources.detail_screen_sunrise_title
import weathertechtask.feature_weather.weatherapp.generated.resources.detail_screen_sunset_title
import weathertechtask.feature_weather.weatherapp.generated.resources.detail_screen_wind_direction_title
import weathertechtask.feature_weather.weatherapp.generated.resources.detail_screen_wind_gust_title
import weathertechtask.feature_weather.weatherapp.generated.resources.detail_screen_wind_speed_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenSuccess(
    dailyForecastDetailModel: DailyForecastDetailModel,
    onBackButtonClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(dailyForecastDetailModel.dt) },
                colors = TopAppBarDefaults.topAppBarColors(titleContentColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClicked.invoke() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_sunrise_title),
                dailyForecastDetailModel.sunrise
            )
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_sunset_title),
                dailyForecastDetailModel.sunset
            )
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_minimum_temperature_title),
                dailyForecastDetailModel.minTemp
            )
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_maximum_temperature_title),
                dailyForecastDetailModel.maxTemp
            )
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_wind_speed_title),
                dailyForecastDetailModel.windSpeed
            )
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_wind_gust_title),
                dailyForecastDetailModel.windGust
            )
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_wind_direction_title),
                dailyForecastDetailModel.windDeg
            )
            DisplayDataVertically(
                stringResource(Res.string.detail_screen_summary_title),
                dailyForecastDetailModel.summary
            )
        }
    }
}
