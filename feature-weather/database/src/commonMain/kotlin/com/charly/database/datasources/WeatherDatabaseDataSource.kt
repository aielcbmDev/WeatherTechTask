package com.charly.database.datasources

import com.charly.database.model.DailyDao
import com.charly.database.model.DailyEntity
import kotlinx.coroutines.flow.Flow

class WeatherDatabaseDataSource(
    private val dailyDao: DailyDao
) {

    suspend fun getDailyWeatherForecastList(): Flow<List<DailyEntity>> {
        return dailyDao.getDailyWeatherForecastList()
    }

    suspend fun insertDailyWeatherForecastList(dailyEntityList: List<DailyEntity>) {
        dailyDao.insertOrReplaceListOfDailyWeatherForecast(dailyEntityList)
    }
}
