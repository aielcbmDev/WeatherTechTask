package com.charly.core.repositories

import com.charly.database.datasources.WeatherDatabaseDataSource
import com.charly.networking.datasource.WeatherNetworkingDataSource

class DailyWeatherRepository(
    private val weatherDatabaseDataSource: WeatherDatabaseDataSource,
    private val weatherNetworkDataSource: WeatherNetworkingDataSource
)
