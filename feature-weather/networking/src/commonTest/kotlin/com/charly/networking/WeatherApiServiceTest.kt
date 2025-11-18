package com.charly.networking

import com.charly.networking.httpclient.HttpClientFactory
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
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
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

private const val WEATHER_JSON_DATA =
    "{\"lat\":40.4983,\"lon\":-3.5676,\"timezone\":\"Europe/Madrid\",\"timezone_offset\":3600,\"daily\":[{\"dt\":1763463600,\"sunrise\":1763449455,\"sunset\":1763484898,\"moonrise\":1763443380,\"moonset\":1763480160,\"moon_phase\":0.95,\"summary\":\"You can expect clear sky in the morning, with partly cloudy in the afternoon\",\"temp\":{\"day\":282.19,\"min\":280.02,\"max\":287.38,\"night\":280.75,\"eve\":284.26,\"morn\":280.06},\"feels_like\":{\"day\":281.54,\"night\":279.32,\"eve\":282.58,\"morn\":279.4},\"pressure\":1020,\"humidity\":75,\"dew_point\":278,\"wind_speed\":2.31,\"wind_deg\":54,\"wind_gust\":3.01,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":0,\"pop\":0,\"uvi\":2.24},{\"dt\":1763550000,\"sunrise\":1763535924,\"sunset\":1763571256,\"moonrise\":1763533560,\"moonset\":1763568240,\"moon_phase\":0.98,\"summary\":\"There will be partly cloudy today\",\"temp\":{\"day\":281.97,\"min\":278.62,\"max\":284.23,\"night\":280.73,\"eve\":283.06,\"morn\":279.03},\"feels_like\":{\"day\":281.97,\"night\":279.49,\"eve\":283.06,\"morn\":277.88},\"pressure\":1017,\"humidity\":44,\"dew_point\":270.13,\"wind_speed\":3.2,\"wind_deg\":23,\"wind_gust\":6.31,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":100,\"pop\":0,\"uvi\":1.93},{\"dt\":1763640000,\"sunrise\":1763622393,\"sunset\":1763657616,\"moonrise\":1763623740,\"moonset\":1763656620,\"moon_phase\":0,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"temp\":{\"day\":283.22,\"min\":278,\"max\":283.63,\"night\":279.23,\"eve\":280.58,\"morn\":278.04},\"feels_like\":{\"day\":281.33,\"night\":276.47,\"eve\":278.23,\"morn\":275.95},\"pressure\":1018,\"humidity\":40,\"dew_point\":269.96,\"wind_speed\":6.13,\"wind_deg\":15,\"wind_gust\":7.02,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":69,\"pop\":0,\"uvi\":2.07},{\"dt\":1763726400,\"sunrise\":1763708862,\"sunset\":1763743978,\"moonrise\":1763713800,\"moonset\":1763745420,\"moon_phase\":0.04,\"summary\":\"You can expect clear sky in the morning, with partly cloudy in the afternoon\",\"temp\":{\"day\":281.53,\"min\":276.6,\"max\":282.13,\"night\":277.42,\"eve\":279.31,\"morn\":276.83},\"feels_like\":{\"day\":279.18,\"night\":275.07,\"eve\":276.37,\"morn\":274.96},\"pressure\":1028,\"humidity\":21,\"dew_point\":260.28,\"wind_speed\":5.22,\"wind_deg\":7,\"wind_gust\":7.52,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":2,\"pop\":0,\"uvi\":2.08},{\"dt\":1763812800,\"sunrise\":1763795331,\"sunset\":1763830341,\"moonrise\":1763803620,\"moonset\":1763834700,\"moon_phase\":0.07,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"temp\":{\"day\":279.73,\"min\":275.32,\"max\":281.38,\"night\":280.99,\"eve\":280.91,\"morn\":275.32},\"feels_like\":{\"day\":278.17,\"night\":279.56,\"eve\":279.85,\"morn\":275.32},\"pressure\":1029,\"humidity\":37,\"dew_point\":266.19,\"wind_speed\":3.21,\"wind_deg\":234,\"wind_gust\":4.24,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":87,\"pop\":0,\"uvi\":1.7},{\"dt\":1763899200,\"sunrise\":1763881798,\"sunset\":1763916707,\"moonrise\":1763893140,\"moonset\":1763924460,\"moon_phase\":0.1,\"summary\":\"There will be partly cloudy today\",\"temp\":{\"day\":285.68,\"min\":279.15,\"max\":287.21,\"night\":282.74,\"eve\":283.8,\"morn\":279.15},\"feels_like\":{\"day\":284.53,\"night\":281.75,\"eve\":282.67,\"morn\":279.15},\"pressure\":1025,\"humidity\":59,\"dew_point\":277.74,\"wind_speed\":3.45,\"wind_deg\":300,\"wind_gust\":5.78,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":100,\"pop\":0,\"uvi\":2},{\"dt\":1763985600,\"sunrise\":1763968265,\"sunset\":1764003075,\"moonrise\":1763982120,\"moonset\":1764014580,\"moon_phase\":0.13,\"summary\":\"There will be partly cloudy today\",\"temp\":{\"day\":284.27,\"min\":280.54,\"max\":284.98,\"night\":283.15,\"eve\":282.98,\"morn\":280.54},\"feels_like\":{\"day\":283.24,\"night\":280.21,\"eve\":280.32,\"morn\":278.52},\"pressure\":1019,\"humidity\":69,\"dew_point\":278.75,\"wind_speed\":6.6,\"wind_deg\":231,\"wind_gust\":11.13,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":96,\"pop\":0,\"uvi\":2},{\"dt\":1764072000,\"sunrise\":1764054732,\"sunset\":1764089445,\"moonrise\":1764070680,\"moonset\":1764104880,\"moon_phase\":0.16,\"summary\":\"There will be partly cloudy today\",\"temp\":{\"day\":288.51,\"min\":282.19,\"max\":288.51,\"night\":282.19,\"eve\":284.84,\"morn\":283.43},\"feels_like\":{\"day\":287.35,\"night\":281.49,\"eve\":283.06,\"morn\":282.94},\"pressure\":1013,\"humidity\":48,\"dew_point\":277.53,\"wind_speed\":7.57,\"wind_deg\":271,\"wind_gust\":11.55,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":93,\"pop\":0,\"uvi\":2}]}"

class WeatherApiServiceTest {

    @Test
    fun `Verify that DailyForecastWeatherData is successfully fetched`() = runTest {
        // GIVEN
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
        val weatherApiKey = "dummyApiKey"
        val weatherApiService = WeatherApiService(
            weatherApiKey = weatherApiKey,
            weatherUnits = WeatherUnits.STANDARD,
            httpClient = httpClientFactory.createHttpClient()
        )
        val latitude = "any latitude"
        val longitude = "any longitude"

        // WHEN
        val result = weatherApiService.getDailyForecastWeatherData(latitude, longitude)

        // THEN
        assertEquals("40.4983", result.lat)
        assertEquals("-3.5676", result.lon)
        assertEquals("Europe/Madrid", result.timezone)
        assertEquals(3600, result.timezoneOffset)
        val daily = result.daily
        assertEquals(8, daily.size)
    }

    @Test
    fun `Verify that requesting DailyForecastWeatherData fails due to a wrong ContentType client setting`() =
        runTest {
            // GIVEN
            val contentType = ContentType.Application.Docx
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
            val weatherApiKey = "dummyApiKey"
            val weatherApiService = WeatherApiService(
                weatherApiKey = weatherApiKey,
                weatherUnits = WeatherUnits.STANDARD,
                httpClient = httpClientFactory.createHttpClient()
            )
            val latitude = "any latitude"
            val longitude = "any longitude"

            // WHEN
            val actualException = assertFailsWith<Exception> {
                weatherApiService.getDailyForecastWeatherData(latitude, longitude)
            }

            // THEN
            assertIs<NoTransformationFoundException>(actualException)
            verifySuspend(mode = VerifyMode.exhaustiveOrder) {
                httpClientFactory.createHttpClient()
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
