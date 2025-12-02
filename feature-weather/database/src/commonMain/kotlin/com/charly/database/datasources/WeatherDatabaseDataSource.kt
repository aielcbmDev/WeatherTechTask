package com.charly.database.datasources

import com.charly.database.OpenClassForMocking
import com.charly.database.model.DailyForecastDao
import com.charly.database.model.DailyForecastEntity
import kotlinx.coroutines.flow.Flow

@OpenClassForMocking
class WeatherDatabaseDataSource(
    private val dailyForecastDao: DailyForecastDao
) {

    fun getDailyWeatherForecastList(): Flow<List<DailyForecastEntity>> {
        return dailyForecastDao.getDailyWeatherForecastList()
    }

    suspend fun getDailyWeatherForecastById(id: Long): DailyForecastEntity {
        return dailyForecastDao.getDailyWeatherForecastById(id)
    }

    suspend fun deleteAndInsertListOfDailyWeatherForecast(dailyForecastEntityList: List<DailyForecastEntity>) {
        dailyForecastDao.deleteAndInsertListOfDailyWeatherForecast(dailyForecastEntityList)
    }
}
