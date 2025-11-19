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
import kotlinx.coroutines.flow.transform

class DailyWeatherForecastRepositoryImpl(
    private val timerCache: TimerCache,
    private val weatherDatabaseDataSource: WeatherDatabaseDataSource,
    private val weatherNetworkDataSource: WeatherNetworkingDataSource
) : DailyWeatherForecastRepository {

    override suspend fun getDailyWeatherForecastList(): Flow<List<Daily>> {
        return weatherDatabaseDataSource.getDailyWeatherForecastList()
            .map { it.mapToDailyList() }.transform {
                if (it.isNotEmpty()) {
                    emit(it)
                }
                if (timerCache.isCacheExpired()) {
                    val dailyWeatherForecastData =
                        weatherNetworkDataSource.getDailyWeatherForecastData()
                    timerCache.saveCacheTime()
                    val dailyEntityList = dailyWeatherForecastData.mapToDailyEntityList()
                    weatherDatabaseDataSource.insertDailyWeatherForecastList(dailyEntityList)
                }
            }
    }
}
