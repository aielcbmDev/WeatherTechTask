package com.charly.database.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyForecastDao {

    @Query("SELECT * FROM daily_weather_table ORDER BY dt")
    fun getDailyWeatherForecastList(): Flow<List<DailyForecastEntity>>

    @Query("SELECT * FROM daily_weather_table WHERE id = :id")
    suspend fun getDailyWeatherForecastById(id: Long): DailyForecastEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceListOfDailyWeatherForecast(dailyForecastEntityList: List<DailyForecastEntity>)

    @Query("DELETE from daily_weather_table")
    suspend fun deleteDailyWeatherForecastTable()

    @Transaction
    suspend fun deleteAndInsertListOfDailyWeatherForecast(dailyForecastEntityList: List<DailyForecastEntity>) {
        deleteDailyWeatherForecastTable()
        insertOrReplaceListOfDailyWeatherForecast(dailyForecastEntityList)
    }
}
