package com.charly.core.repositories

import com.charly.core.cache.TimerCache
import com.charly.core.mappers.database.mapToDailyForecastList
import com.charly.core.mappers.networking.mapToDailyForecastEntityList
import com.charly.database.datasources.WeatherDatabaseDataSource
import com.charly.domain.model.DailyForecast
import com.charly.domain.repositories.GetDailyWeatherForecastListRepository
import com.charly.networking.datasource.WeatherNetworkingDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class GetDailyWeatherForecastListRepositoryImpl(
    private val timerCache: TimerCache,
    private val weatherDatabaseDataSource: WeatherDatabaseDataSource,
    private val weatherNetworkDataSource: WeatherNetworkingDataSource
) : GetDailyWeatherForecastListRepository {

    override suspend fun execute(): Flow<List<DailyForecast>> {
        return weatherDatabaseDataSource.getDailyWeatherForecastList()
            .map { it.mapToDailyForecastList() }
            .transform {
                emit(it)
                fetchAndSaveDailyWeatherForecast()
            }
    }

    private suspend fun fetchAndSaveDailyWeatherForecast() {
        if (timerCache.isCacheExpired()) {
            val dailyWeatherForecastData = weatherNetworkDataSource.getDailyWeatherForecastData()
            timerCache.saveCacheTime()
            weatherDatabaseDataSource.deleteDailyWeatherForecastTable()
            val dailyEntityList = dailyWeatherForecastData.mapToDailyForecastEntityList()
            weatherDatabaseDataSource.insertDailyWeatherForecastList(dailyEntityList)
        }
    }
}
