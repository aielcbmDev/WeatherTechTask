package com.charly.networking.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyForecastData(

    @SerialName("dt")
    val dt: Long? = null,
    @SerialName("sunrise")
    val sunrise: Long? = null,
    @SerialName("sunset")
    val sunset: Long? = null,
    @SerialName("moonrise")
    val moonrise: Long? = null,
    @SerialName("moonset")
    val moonset: Long? = null,
    @SerialName("moon_phase")
    val moonPhase: String? = null,
    @SerialName("summary")
    val summary: String? = null,
    @SerialName("temp")
    val temp: TempData? = TempData(),
    @SerialName("feels_like")
    val feelsLike: FeelsLikeData? = null,
    @SerialName("pressure")
    val pressure: String? = null,
    @SerialName("humidity")
    val humidity: String? = null,
    @SerialName("dew_point")
    val dewPoint: String? = null,
    @SerialName("wind_speed")
    val windSpeed: String? = null,
    @SerialName("wind_deg")
    val windDeg: String? = null,
    @SerialName("wind_gust")
    val windGust: String? = null,
    @SerialName("weather")
    val weather: ArrayList<WeatherData> = arrayListOf(),
    @SerialName("clouds")
    val clouds: String? = null,
    @SerialName("pop")
    val pop: String? = null,
    @SerialName("uvi")
    val uvi: String? = null
)
