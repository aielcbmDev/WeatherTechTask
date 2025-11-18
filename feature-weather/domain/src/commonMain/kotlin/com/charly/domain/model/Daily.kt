package com.charly.domain.model

data class Daily(
    val dt: Int? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null,
    val summary: String? = null,
    val minTemp: Double? = null,
    val maxTemp: Double? = null,
    val weatherDescription: String? = null,
    val weatherIcon: String? = null
)
