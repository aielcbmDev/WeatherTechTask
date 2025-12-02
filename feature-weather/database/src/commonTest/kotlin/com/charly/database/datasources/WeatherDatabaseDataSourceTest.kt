package com.charly.database.datasources

import com.charly.database.model.DailyForecastDao
import com.charly.database.model.DailyForecastEntity
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertSame

private const val DAILY_WEATHER_FORECAST_LIST =
    "[{\"id\":1,\"dt\":1763827200,\"sunrise\":1763812283,\"sunset\":1763847206,\"summary\":\"Expect a day of partly cloudy with rain\",\"minTemp\":\"5.64\",\"maxTemp\":\"10.6\",\"windSpeed\":\"5.44\",\"windDeg\":\"338\",\"windGust\":\"8.32\"},{\"id\":2,\"dt\":1763913600,\"sunrise\":1763898751,\"sunset\":1763933572,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"4.08\",\"maxTemp\":\"9.62\",\"windSpeed\":\"6.22\",\"windDeg\":\"294\",\"windGust\":\"12.17\"},{\"id\":3,\"dt\":1764000000,\"sunrise\":1763985218,\"sunset\":1764019940,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"5.36\",\"maxTemp\":\"9.79\",\"windSpeed\":\"6.75\",\"windDeg\":\"325\",\"windGust\":\"12.25\"},{\"id\":4,\"dt\":1764086400,\"sunrise\":1764071685,\"sunset\":1764106310,\"summary\":\"You can expect partly cloudy in the morning, with rain in the afternoon\",\"minTemp\":\"6.12\",\"maxTemp\":\"10.14\",\"windSpeed\":\"3.46\",\"windDeg\":\"168\",\"windGust\":\"10.93\"},{\"id\":5,\"dt\":1764172800,\"sunrise\":1764158151,\"sunset\":1764192682,\"summary\":\"You can expect partly cloudy in the morning, with rain in the afternoon\",\"minTemp\":\"10.07\",\"maxTemp\":\"12.87\",\"windSpeed\":\"2.3\",\"windDeg\":\"241\",\"windGust\":\"7.04\"},{\"id\":6,\"dt\":1764259200,\"sunrise\":1764244616,\"sunset\":1764279057,\"summary\":\"Expect a day of partly cloudy with rain\",\"minTemp\":\"3.29\",\"maxTemp\":\"13.01\",\"windSpeed\":\"8.51\",\"windDeg\":\"271\",\"windGust\":\"12.81\"},{\"id\":7,\"dt\":1764345600,\"sunrise\":1764331080,\"sunset\":1764365433,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"1.01\",\"maxTemp\":\"4.8\",\"windSpeed\":\"9\",\"windDeg\":\"274\",\"windGust\":\"14.63\"},{\"id\":8,\"dt\":1764432000,\"sunrise\":1764417544,\"sunset\":1764451812,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"1.15\",\"maxTemp\":\"5.62\",\"windSpeed\":\"7.54\",\"windDeg\":\"305\",\"windGust\":\"14.61\"}]"

private const val SINGLE_DAILY_WEATHER_FORECAST =
    "{\"id\":4,\"dt\":1764086400,\"sunrise\":1764071685,\"sunset\":1764106310,\"summary\":\"You can expect partly cloudy in the morning, with rain in the afternoon\",\"minTemp\":\"6.42\",\"maxTemp\":\"10.61\",\"windSpeed\":\"5.4\",\"windDeg\":\"152\",\"windGust\":\"11.57\"}"

class WeatherDatabaseDataSourceTest {

    @Test
    fun `Verify that requesting the daily weather forecast succeeds`() = runTest {
        // GIVEN
        val expectedResult =
            Json.decodeFromString<List<DailyForecastEntity>>(DAILY_WEATHER_FORECAST_LIST)
        val dailyForecastDao = mock<DailyForecastDao> {
            every { getDailyWeatherForecastList() } returns flowOf(expectedResult)
        }
        val weatherDatabaseDataSource = WeatherDatabaseDataSource(dailyForecastDao)

        // WHEN
        val result = weatherDatabaseDataSource.getDailyWeatherForecastList().first()

        // THEN
        assertSame(expectedResult, result)
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            @Suppress("UnusedFlow")
            dailyForecastDao.getDailyWeatherForecastList()
        }
    }

    @Test
    fun `Verify that requesting a single daily weather forecast by id succeeds`() = runTest {
        // GIVEN
        val id = 4L
        val expectedResult =
            Json.decodeFromString<DailyForecastEntity>(SINGLE_DAILY_WEATHER_FORECAST)
        val dailyForecastDao = mock<DailyForecastDao> {
            everySuspend { getDailyWeatherForecastById(id) } returns expectedResult
        }
        val weatherDatabaseDataSource = WeatherDatabaseDataSource(dailyForecastDao)

        // WHEN
        val result = weatherDatabaseDataSource.getDailyWeatherForecastById(id)

        // THEN
        assertSame(expectedResult, result)
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            dailyForecastDao.getDailyWeatherForecastById(id)
        }
    }

    @Test
    fun `Verify that delete and insert the daily weather forecast table succeeds`() = runTest {
        // GIVEN
        val dailyForecastEntityList =
            Json.decodeFromString<List<DailyForecastEntity>>(DAILY_WEATHER_FORECAST_LIST)
        val dailyForecastDao = mock<DailyForecastDao> {
            everySuspend { deleteAndInsertListOfDailyWeatherForecast(dailyForecastEntityList) } returns Unit
        }
        val weatherDatabaseDataSource = WeatherDatabaseDataSource(dailyForecastDao)

        // WHEN
        weatherDatabaseDataSource.deleteAndInsertListOfDailyWeatherForecast(dailyForecastEntityList)

        // THEN
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            dailyForecastDao.deleteAndInsertListOfDailyWeatherForecast(dailyForecastEntityList)
        }
    }
}
