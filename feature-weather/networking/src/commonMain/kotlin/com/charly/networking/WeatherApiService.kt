package com.charly.networking

import com.charly.networking.model.DailyForecastWeatherData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WeatherApiService(
    private val weatherApiKey: String,
    private val weatherUnits: WeatherUnits,
    private val httpClient: HttpClient
) {

    suspend fun getDailyWeatherForecastData(): DailyForecastWeatherData {
        val url = generateUrl(weatherUnits, weatherApiKey)
        return httpClient.get(url).body<DailyForecastWeatherData>()
    }

    private companion object Companion {

        private const val HARDCODED_LONGITUDE = "-74.0060"
        private const val HARDCODED_LATITUDE = "40.7128"

        fun generateUrl(
            weatherUnits: WeatherUnits,
            weatherApiKey: String
        ): String {
            return "https://api.openweathermap.org/data/3.0/onecall?lat=$HARDCODED_LATITUDE&lon=$HARDCODED_LONGITUDE&units=${weatherUnits.units}&exclude=current,minutely,hourly,alerts&appid=$weatherApiKey"
        }
    }
}
