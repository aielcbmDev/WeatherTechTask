package com.charly.weatherapp.model

import androidx.compose.runtime.Immutable

@Immutable
data class DailyForecastModel(
    val id: Long = 0,
    val dt: String,
    val sunrise: String,
    val sunset: String,
    val summary: String,
    val minTemp: String,
    val maxTemp: String
)
