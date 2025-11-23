package com.charly.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DailyForecast(
    val id: Long = 0,
    val dt: Long? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val summary: String? = null,
    val minTemp: String? = null,
    val maxTemp: String? = null,
    val windSpeed: String? = null,
    val windDeg: String? = null,
    val windGust: String? = null
)
