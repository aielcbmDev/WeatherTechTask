package com.charly.database.datasources

import com.charly.database.model.DailyDao
import com.charly.database.model.DailyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherDatabaseDataSource(
    private val dailyDao: DailyDao
) {

    suspend fun getDailyWeatherForecastList(): Flow<List<DailyEntity>> {
        return flowOf(dailyDao.getDailyWeatherForecastList())
    }

    suspend fun insertDailyWeatherForecastList(dailyEntityList: List<DailyEntity>) {
        dailyDao.insertOrReplaceListOfDailyWeatherForecast(dailyEntityList)
    }
}
