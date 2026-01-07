package com.charly.networking

import com.charly.networking.model.DailyForecastWeatherData

fun interface WeatherApiService {

    suspend fun getDailyWeatherForecastData(): DailyForecastWeatherData
}
