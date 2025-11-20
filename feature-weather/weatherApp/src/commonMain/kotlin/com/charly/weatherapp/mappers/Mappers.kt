package com.charly.weatherapp.mappers

import com.charly.domain.model.Daily
import com.charly.weatherapp.formatdata.TimeFormatter
import com.charly.weatherapp.model.DailyForecastModel

internal fun Daily.mapToDailyForecastModel(
    timeFormatter: TimeFormatter,
    noDataAvailable: String
): DailyForecastModel {
    return DailyForecastModel(
        id = id,
        dt = timeFormatter.formatEpochSeconds(dt) ?: noDataAvailable,
        sunrise = timeFormatter.formatEpochSeconds(sunrise) ?: noDataAvailable,
        sunset = timeFormatter.formatEpochSeconds(sunset) ?: noDataAvailable,
        summary = summary ?: noDataAvailable,
        minTemp = minTemp ?: noDataAvailable,
        maxTemp = maxTemp ?: noDataAvailable
    )
}

internal fun List<Daily>.mapToDailyForecastModelList(
    timeFormatter: TimeFormatter,
    noDataAvailable: String
): List<DailyForecastModel> {
    return map { it.mapToDailyForecastModel(timeFormatter, noDataAvailable) }
}
