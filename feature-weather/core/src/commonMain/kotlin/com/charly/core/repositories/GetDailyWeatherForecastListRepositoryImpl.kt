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

    private var isCacheInvalid = false

    override suspend fun execute(invalidateCache: Boolean): Flow<List<DailyForecast>> {
        this.isCacheInvalid = invalidateCache
        return weatherDatabaseDataSource.getDailyWeatherForecastList()
            .map { it.mapToDailyForecastList() }
            .transform {
                if (it.isNotEmpty()) {
                    emit(it)
                }
                fetchAndSaveDailyWeatherForecast()
            }
    }

    private suspend fun fetchAndSaveDailyWeatherForecast() {
        if (isCacheInvalid || timerCache.isCacheExpired()) {
            try {
                val dailyWeatherForecastData =
                    weatherNetworkDataSource.getDailyWeatherForecastData()
                val dailyEntityList = dailyWeatherForecastData.mapToDailyForecastEntityList()
                weatherDatabaseDataSource.deleteAndInsertListOfDailyWeatherForecast(dailyEntityList)
                timerCache.saveCacheTime()
                isCacheInvalid = false
            } catch (e: Exception) {
                isCacheInvalid = false
                throw e
            }
        }
    }
}
