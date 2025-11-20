package com.charly.core.repositories

import com.charly.core.cache.TimerCache
import com.charly.core.mappers.database.mapToDailyList
import com.charly.core.mappers.networking.mapToDailyEntityList
import com.charly.database.datasources.WeatherDatabaseDataSource
import com.charly.domain.model.Daily
import com.charly.domain.repositories.DailyWeatherForecastRepository
import com.charly.networking.datasource.WeatherNetworkingDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class DailyWeatherForecastRepositoryImpl(
    private val timerCache: TimerCache,
    private val weatherDatabaseDataSource: WeatherDatabaseDataSource,
    private val weatherNetworkDataSource: WeatherNetworkingDataSource
) : DailyWeatherForecastRepository {

    override suspend fun getDailyWeatherForecastList(): Flow<List<Daily>> {
        return weatherDatabaseDataSource.getDailyWeatherForecastList()
            .onStart { fetchAndSaveDailyWeatherForecast() }
            .map { it.mapToDailyList() }
    }

    private suspend fun fetchAndSaveDailyWeatherForecast() {
        if (timerCache.isCacheExpired()) {
            val dailyWeatherForecastData = weatherNetworkDataSource.getDailyWeatherForecastData()
            timerCache.saveCacheTime()
            weatherDatabaseDataSource.deleteDailyWeatherForecastTable()
            val dailyEntityList = dailyWeatherForecastData.mapToDailyEntityList()
            weatherDatabaseDataSource.insertDailyWeatherForecastList(dailyEntityList)
        }
    }
}
