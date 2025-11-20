package com.charly.weatherapp.ui.detailscreen.success

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.charly.weatherapp.model.DailyForecastModel
import com.charly.weatherapp.ui.common.DisplayDataHorizontally
import com.charly.weatherapp.ui.common.DisplayDataVertically
import org.jetbrains.compose.resources.stringResource
import volkswagentechtask.feature_weather.weatherapp.generated.resources.Res
import volkswagentechtask.feature_weather.weatherapp.generated.resources.detail_screen_maximum_temperature_title
import volkswagentechtask.feature_weather.weatherapp.generated.resources.detail_screen_minimum_temperature_title
import volkswagentechtask.feature_weather.weatherapp.generated.resources.detail_screen_summary_title
import volkswagentechtask.feature_weather.weatherapp.generated.resources.detail_screen_sunrise_title
import volkswagentechtask.feature_weather.weatherapp.generated.resources.detail_screen_sunset_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenSuccess(
    dailyForecastModel: DailyForecastModel,
    onBackButtonClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(dailyForecastModel.dt) },
                colors = TopAppBarDefaults.topAppBarColors(titleContentColor = Color.Red),
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClicked.invoke() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(padding)
        ) {
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_sunrise_title),
                dailyForecastModel.sunrise
            )
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_sunset_title),
                dailyForecastModel.sunset
            )
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_minimum_temperature_title),
                dailyForecastModel.minTemp
            )
            DisplayDataHorizontally(
                stringResource(Res.string.detail_screen_maximum_temperature_title),
                dailyForecastModel.maxTemp
            )
            DisplayDataVertically(
                stringResource(Res.string.detail_screen_summary_title),
                dailyForecastModel.summary
            )
        }
    }
}
