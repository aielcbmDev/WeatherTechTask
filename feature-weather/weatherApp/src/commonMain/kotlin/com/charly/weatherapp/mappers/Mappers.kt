package com.charly.weatherapp.mappers

import com.charly.domain.model.DailyForecast
import com.charly.weatherapp.formatdata.TimeFormatter
import com.charly.weatherapp.model.DailyForecastModel

internal fun DailyForecast.mapToDailyForecastModel(
    timeFormatter: TimeFormatter,
    noDataAvailable: String
): DailyForecastModel {
    return DailyForecastModel(
        id = id,
        dt = timeFormatter.formatEpochSecondsToString(dt) ?: noDataAvailable,
        sunrise = timeFormatter.formatEpochSecondsToString(sunrise) ?: noDataAvailable,
        sunset = timeFormatter.formatEpochSecondsToString(sunset) ?: noDataAvailable,
        summary = summary ?: noDataAvailable,
        minTemp = minTemp ?: noDataAvailable,
        maxTemp = maxTemp ?: noDataAvailable
    )
}

internal fun List<DailyForecast>.mapToDailyForecastModelList(
    timeFormatter: TimeFormatter,
    noDataAvailable: String
): List<DailyForecastModel> {
    return map { it.mapToDailyForecastModel(timeFormatter, noDataAvailable) }
}
