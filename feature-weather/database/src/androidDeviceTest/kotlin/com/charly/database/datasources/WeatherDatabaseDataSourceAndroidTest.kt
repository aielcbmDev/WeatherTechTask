package com.charly.database.datasources

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.charly.database.WeatherDatabase
import com.charly.database.model.DailyForecastDao
import com.charly.database.model.DailyForecastEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val DAILY_WEATHER_FORECAST_LIST =
    "[{\"id\":1,\"dt\":1763827200,\"sunrise\":1763812283,\"sunset\":1763847206,\"summary\":\"Expect a day of partly cloudy with rain\",\"minTemp\":\"5.64\",\"maxTemp\":\"10.6\",\"windSpeed\":\"5.44\",\"windDeg\":\"338\",\"windGust\":\"8.32\"},{\"id\":2,\"dt\":1763913600,\"sunrise\":1763898751,\"sunset\":1763933572,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"4.08\",\"maxTemp\":\"9.62\",\"windSpeed\":\"6.22\",\"windDeg\":\"294\",\"windGust\":\"12.17\"},{\"id\":3,\"dt\":1764000000,\"sunrise\":1763985218,\"sunset\":1764019940,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"5.36\",\"maxTemp\":\"9.79\",\"windSpeed\":\"6.75\",\"windDeg\":\"325\",\"windGust\":\"12.25\"},{\"id\":4,\"dt\":1764086400,\"sunrise\":1764071685,\"sunset\":1764106310,\"summary\":\"You can expect partly cloudy in the morning, with rain in the afternoon\",\"minTemp\":\"6.12\",\"maxTemp\":\"10.14\",\"windSpeed\":\"3.46\",\"windDeg\":\"168\",\"windGust\":\"10.93\"},{\"id\":5,\"dt\":1764172800,\"sunrise\":1764158151,\"sunset\":1764192682,\"summary\":\"You can expect partly cloudy in the morning, with rain in the afternoon\",\"minTemp\":\"10.07\",\"maxTemp\":\"12.87\",\"windSpeed\":\"2.3\",\"windDeg\":\"241\",\"windGust\":\"7.04\"},{\"id\":6,\"dt\":1764259200,\"sunrise\":1764244616,\"sunset\":1764279057,\"summary\":\"Expect a day of partly cloudy with rain\",\"minTemp\":\"3.29\",\"maxTemp\":\"13.01\",\"windSpeed\":\"8.51\",\"windDeg\":\"271\",\"windGust\":\"12.81\"},{\"id\":7,\"dt\":1764345600,\"sunrise\":1764331080,\"sunset\":1764365433,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"1.01\",\"maxTemp\":\"4.8\",\"windSpeed\":\"9\",\"windDeg\":\"274\",\"windGust\":\"14.63\"},{\"id\":8,\"dt\":1764432000,\"sunrise\":1764417544,\"sunset\":1764451812,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"1.15\",\"maxTemp\":\"5.62\",\"windSpeed\":\"7.54\",\"windDeg\":\"305\",\"windGust\":\"14.61\"}]"

@RunWith(AndroidJUnit4::class)
class WeatherDatabaseDataSourceAndroidTest {

    private lateinit var weatherDatabase: WeatherDatabase
    private lateinit var dailyForecastDao: DailyForecastDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        weatherDatabase = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java).build()
        dailyForecastDao = weatherDatabase.getDailyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        weatherDatabase.close()
    }

    @Test
    fun verify_that_daily_weather_database_data_is_saved_and_retrieved_successfully() = runTest {
        // GIVEN
        val dailyForecastEntityList =
            Json.decodeFromString<List<DailyForecastEntity>>(DAILY_WEATHER_FORECAST_LIST)
        dailyForecastDao.insertOrReplaceListOfDailyWeatherForecast(dailyForecastEntityList)

        // WHEN
        val result = dailyForecastDao.getDailyWeatherForecastList().first()

        // THEN
        assertContentEquals(dailyForecastEntityList, result)
    }

    @Test
    fun verify_that_requesting_a_single_daily_weather_forecast_by_id_succeeds() = runTest {
        // GIVEN
        val expectedItemId = 7L
        val dailyForecastEntityList =
            Json.decodeFromString<List<DailyForecastEntity>>(DAILY_WEATHER_FORECAST_LIST)
        dailyForecastDao.insertOrReplaceListOfDailyWeatherForecast(dailyForecastEntityList)

        // WHEN
        val result = dailyForecastDao.getDailyWeatherForecastById(expectedItemId)

        // THEN
        assertEquals(expectedItemId, result.id)
        assertEquals(1764345600, result.dt)
        assertEquals(1764331080, result.sunrise)
        assertEquals(1764365433, result.sunset)
        assertEquals("Expect a day of partly cloudy with clear spells", result.summary)
        assertEquals("1.01", result.minTemp)
        assertEquals("4.8", result.maxTemp)
        assertEquals("9", result.windSpeed)
        assertEquals("274", result.windDeg)
        assertEquals("14.63", result.windGust)
    }

    @Test
    fun verify_that_deleting_the_daily_weather_forecast_table_succeeds() = runTest {
        // GIVEN
        val dailyForecastEntityList =
            Json.decodeFromString<List<DailyForecastEntity>>(DAILY_WEATHER_FORECAST_LIST)
        dailyForecastDao.insertOrReplaceListOfDailyWeatherForecast(dailyForecastEntityList)

        // WHEN
        dailyForecastDao.deleteDailyWeatherForecastTable()

        // THEN
        val result = dailyForecastDao.getDailyWeatherForecastList().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun verify_that_delete_and_insert_the_daily_weather_forecast_table_succeeds() = runTest {
        // GIVEN
        val dailyForecastEntityList =
            Json.decodeFromString<List<DailyForecastEntity>>(DAILY_WEATHER_FORECAST_LIST)
        dailyForecastDao.insertOrReplaceListOfDailyWeatherForecast(dailyForecastEntityList)

        // WHEN
        dailyForecastDao.deleteAndInsertListOfDailyWeatherForecast(dailyForecastEntityList)
        val result = dailyForecastDao.getDailyWeatherForecastList().first()

        // THEN
        assertContentEquals(dailyForecastEntityList, result)
    }
}
