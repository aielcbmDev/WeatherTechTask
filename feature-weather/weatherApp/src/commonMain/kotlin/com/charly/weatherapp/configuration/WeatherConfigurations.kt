package com.charly.weatherapp.configuration

import com.charly.weatherapp.model.WeatherUnits
import kotlinx.datetime.TimeZone

object WeatherConfigurations {
    const val WEATHER_CACHE_TIME_IN_MILLIS = 7200000L // 2 hours
    val weatherUnits: WeatherUnits = WeatherUnits.METRIC
    val timeZone: TimeZone = TimeZone.UTC
}
