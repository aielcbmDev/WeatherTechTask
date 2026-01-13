package com.charly.weatherapp.model

/**
 * Defines the units of measurement for weather data returned by the API.
 * This affects values like temperature and wind speed.
 *
 * The following unit systems are supported:
 * - [STANDARD]: The default system.
 *   - Temperature: Kelvin (K)
 *   - Wind Speed: meter/sec
 * - [METRIC]:
 *   - Temperature: Celsius (°C)
 *   - Wind Speed: meter/sec
 * - [IMPERIAL]:
 *   - Temperature: Fahrenheit (°F)
 *   - Wind Speed: miles/hour
 */
enum class WeatherUnits(val units: String) {
    STANDARD("standard"),
    METRIC("metric"),
    IMPERIAL("imperial")
}
