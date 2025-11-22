package com.charly.networking.datasource

import com.charly.networking.WeatherApiService
import com.charly.networking.httpclient.HttpClientFactory
import com.charly.networking.model.DailyForecastWeatherData
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

private const val WEATHER_JSON_DATA =
    "{\"lat\":\"40.7128\",\"lon\":\"-74.006\",\"timezone\":\"America/New_York\",\"timezone_offset\":-18000,\"daily\":[{\"dt\":1763827200,\"sunrise\":1763812283,\"sunset\":1763847206,\"moonrise\":1763821260,\"moonset\":1763852160,\"moon_phase\":\"0.07\",\"summary\":\"Expect a day of partly cloudy with rain\",\"temp\":{\"day\":\"9.37\",\"min\":\"5.69\",\"max\":\"10.81\",\"night\":\"5.69\",\"eve\":\"10.34\",\"morn\":\"8.85\"},\"feels_like\":{\"day\":\"7.86\",\"night\":\"3.31\",\"eve\":\"9\",\"morn\":\"7.7\"},\"pressure\":\"1012\",\"humidity\":\"77\",\"dew_point\":\"5.55\",\"wind_speed\":\"5.51\",\"wind_deg\":\"326\",\"wind_gust\":\"8.17\",\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":\"99\",\"pop\":\"1\",\"uvi\":\"1.41\"},{\"dt\":1763913600,\"sunrise\":1763898751,\"sunset\":1763933572,\"moonrise\":1763910600,\"moonset\":1763941980,\"moon_phase\":\"0.1\",\"summary\":\"Expect a day of partly cloudy with clear spells\",\"temp\":{\"day\":\"7.23\",\"min\":\"4.22\",\"max\":\"10.19\",\"night\":\"6.79\",\"eve\":\"9.19\",\"morn\":\"4.49\"},\"feels_like\":{\"day\":\"4.72\",\"night\":\"3.47\",\"eve\":\"6.58\",\"morn\":\"4.49\"},\"pressure\":\"1016\",\"humidity\":\"48\",\"dew_point\":\"-3.21\",\"wind_speed\":\"5.98\",\"wind_deg\":\"294\",\"wind_gust\":\"11.24\",\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"clouds\":\"20\",\"pop\":\"0\",\"uvi\":\"1.51\"},{\"dt\":1764000000,\"sunrise\":1763985218,\"sunset\":1764019940,\"moonrise\":1763999520,\"moonset\":1764032160,\"moon_phase\":\"0.13\",\"summary\":\"Expect a day of partly cloudy with clear spells\",\"temp\":{\"day\":\"7.73\",\"min\":\"5.28\",\"max\":\"9.8\",\"night\":\"7.27\",\"eve\":\"8.96\",\"morn\":\"5.72\"},\"feels_like\":{\"day\":\"4.37\",\"night\":\"7.27\",\"eve\":\"7.54\",\"morn\":\"1.85\"},\"pressure\":\"1023\",\"humidity\":\"47\",\"dew_point\":\"-3.05\",\"wind_speed\":\"6.44\",\"wind_deg\":\"328\",\"wind_gust\":\"12.64\",\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":\"10\",\"pop\":\"0\",\"uvi\":\"1.99\"},{\"dt\":1764086400,\"sunrise\":1764071685,\"sunset\":1764106310,\"moonrise\":1764088020,\"moonset\":1764122520,\"moon_phase\":\"0.17\",\"summary\":\"You can expect partly cloudy in the morning, with rain in the afternoon\",\"temp\":{\"day\":\"9.44\",\"min\":\"6.42\",\"max\":\"10.61\",\"night\":\"10.61\",\"eve\":\"8.59\",\"morn\":\"6.46\"},\"feels_like\":{\"day\":\"7.41\",\"night\":\"10.31\",\"eve\":\"6.39\",\"morn\":\"4.85\"},\"pressure\":\"1024\",\"humidity\":\"54\",\"dew_point\":\"0.56\",\"wind_speed\":\"5.4\",\"wind_deg\":\"152\",\"wind_gust\":\"11.57\",\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":\"100\",\"pop\":\"1\",\"uvi\":\"1.14\"},{\"dt\":1764172800,\"sunrise\":1764158151,\"sunset\":1764192682,\"moonrise\":1764176160,\"moonset\":1764212940,\"moon_phase\":\"0.2\",\"summary\":\"Expect a day of partly cloudy with rain\",\"temp\":{\"day\":\"11.87\",\"min\":\"10.85\",\"max\":\"13.48\",\"night\":\"12.82\",\"eve\":\"13.4\",\"morn\":\"10.95\"},\"feels_like\":{\"day\":\"11.57\",\"night\":\"12.77\",\"eve\":\"13.28\",\"morn\":\"10.66\"},\"pressure\":\"1015\",\"humidity\":\"94\",\"dew_point\":\"10.83\",\"wind_speed\":\"2.51\",\"wind_deg\":\"223\",\"wind_gust\":\"10.11\",\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":\"100\",\"pop\":\"1\",\"uvi\":\"1.52\"},{\"dt\":1764259200,\"sunrise\":1764244616,\"sunset\":1764279057,\"moonrise\":1764264060,\"moonset\":1764303420,\"moon_phase\":\"0.23\",\"summary\":\"Expect a day of partly cloudy with rain\",\"temp\":{\"day\":\"5.99\",\"min\":\"2.58\",\"max\":\"13.71\",\"night\":\"2.58\",\"eve\":\"5.91\",\"morn\":\"9.42\"},\"feels_like\":{\"day\":\"1.43\",\"night\":\"-3.19\",\"eve\":\"1.27\",\"morn\":\"5.78\"},\"pressure\":\"1011\",\"humidity\":\"50\",\"dew_point\":\"-3.73\",\"wind_speed\":\"8.71\",\"wind_deg\":\"268\",\"wind_gust\":\"14.36\",\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":\"20\",\"pop\":\"1\",\"uvi\":\"2\"},{\"dt\":1764345600,\"sunrise\":1764331080,\"sunset\":1764365433,\"moonrise\":1764351900,\"moonset\":0,\"moon_phase\":\"0.25\",\"summary\":\"Expect a day of partly cloudy with clear spells\",\"temp\":{\"day\":\"2.56\",\"min\":\"0.97\",\"max\":\"5.28\",\"night\":\"2.58\",\"eve\":\"5.28\",\"morn\":\"1.17\"},\"feels_like\":{\"day\":\"-3.5\",\"night\":\"-2.79\",\"eve\":\"0.39\",\"morn\":\"-5.11\"},\"pressure\":\"1015\",\"humidity\":\"51\",\"dew_point\":\"-6.74\",\"wind_speed\":\"9.6\",\"wind_deg\":\"257\",\"wind_gust\":\"15.7\",\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"clouds\":\"23\",\"pop\":\"0\",\"uvi\":\"2\"},{\"dt\":1764432000,\"sunrise\":1764417544,\"sunset\":1764451812,\"moonrise\":1764439620,\"moonset\":1764393900,\"moon_phase\":\"0.3\",\"summary\":\"You can expect clear sky in the morning, with partly cloudy in the afternoon\",\"temp\":{\"day\":\"2.83\",\"min\":\"0.83\",\"max\":\"5.87\",\"night\":\"3.8\",\"eve\":\"5.87\",\"morn\":\"1.09\"},\"feels_like\":{\"day\":\"-2.1\",\"night\":\"1.65\",\"eve\":\"2.54\",\"morn\":\"-4.51\"},\"pressure\":\"1028\",\"humidity\":\"47\",\"dew_point\":\"-7.51\",\"wind_speed\":\"7.2\",\"wind_deg\":\"273\",\"wind_gust\":\"13.66\",\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":\"6\",\"pop\":\"0\",\"uvi\":\"2\"}]}"

class WeatherNetworkingDataSourceTest {

    @Test
    fun `Verify that execute succeeds`() = runTest {
        // GIVEN
        val expectedResult = Json.decodeFromString<DailyForecastWeatherData>(WEATHER_JSON_DATA)
        val contentType = ContentType.Any
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(WEATHER_JSON_DATA),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }
        val httpClientFactory = mock<HttpClientFactory> {
            every { createHttpClient() } returns createDummyHttpClient(mockEngine, contentType)
        }
        val weatherApiService =
            WeatherApiService("whatever-key", "imperial", httpClientFactory.createHttpClient())
        val weatherNetworkingDataSource = WeatherNetworkingDataSource(weatherApiService)

        // WHEN
        val result = weatherNetworkingDataSource.getDailyWeatherForecastData()

        // THEN
        assertEquals(expectedResult, result)
        assertContentEquals(expectedResult.daily, result.daily)
    }

    @Test
    fun `Verify that execute fails`() = runTest {
        // GIVEN
        val weatherApiService = mock<WeatherApiService> {
            everySuspend { getDailyWeatherForecastData() } throws Exception()
        }
        val weatherNetworkingDataSource = WeatherNetworkingDataSource(weatherApiService)

        // WHEN
        val actualException = assertFailsWith<Exception> {
            weatherNetworkingDataSource.getDailyWeatherForecastData()
        }

        // THEN
        assertIs<Exception>(actualException)
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            weatherApiService.getDailyWeatherForecastData()
        }
    }

    private fun createDummyHttpClient(
        mockEngine: MockEngine,
        contentType: ContentType
    ): HttpClient {
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(
                    contentType = contentType,
                    json = Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
}
