package com.charly.networking.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyData(

    @SerialName("dt")
    val dt: Int? = null,
    @SerialName("sunrise")
    val sunrise: Int? = null,
    @SerialName("sunset")
    val sunset: Int? = null,
    @SerialName("moonrise")
    val moonrise: Int? = null,
    @SerialName("moonset")
    val moonset: Int? = null,
    @SerialName("moon_phase")
    val moonPhase: Double? = null,
    @SerialName("summary")
    val summary: String? = null,
    @SerialName("temp")
    val temp: TempData? = TempData(),
    @SerialName("feels_like")
    val feelsLike: FeelsLikeData? = null,
    @SerialName("pressure")
    val pressure: Int? = null,
    @SerialName("humidity")
    val humidity: Int? = null,
    @SerialName("dew_point")
    val dewPoint: Double? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null,
    @SerialName("wind_deg")
    val windDeg: Int? = null,
    @SerialName("wind_gust")
    val windGust: Double? = null,
    @SerialName("weather")
    val weather: ArrayList<WeatherData> = arrayListOf(),
    @SerialName("clouds")
    val clouds: Int? = null,
    @SerialName("pop")
    val pop: Int? = null,
    @SerialName("uvi")
    val uvi: Double? = null
)
