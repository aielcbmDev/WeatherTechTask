package com.something.volkswagentechtask

import platform.Foundation.NSBundle

private const val WEATHER_API_KEY = "WEATHER_API_KEY"
private const val DEFAULT_API_KEY = "DEFAULT_API_KEY"

class WeatherApiSecretsImpl : WeatherApiSecrets {

    override fun getWeatherApiKey(): String {
        val weatherApiKey =
            NSBundle.mainBundle.objectForInfoDictionaryKey(WEATHER_API_KEY) as? String
        return if (weatherApiKey.isNullOrEmpty()) DEFAULT_API_KEY else weatherApiKey
    }
}
