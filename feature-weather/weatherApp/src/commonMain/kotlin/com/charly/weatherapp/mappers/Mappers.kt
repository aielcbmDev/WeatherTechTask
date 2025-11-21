package com.charly.weatherapp.mappers

import com.charly.domain.model.DailyForecast
import com.charly.weatherapp.formatdata.datetime.DateFormatter
import com.charly.weatherapp.formatdata.datetime.TimeFormatter
import com.charly.weatherapp.model.DailyForecastModel

internal fun DailyForecast.mapToDailyForecastModel(
    dateFormatter: DateFormatter,
    timeFormatter: TimeFormatter,
    noDataAvailable: String
): DailyForecastModel {
    return DailyForecastModel(
        id = id,
        dt = dateFormatter.formatEpochSecondsToDateString(dt) ?: noDataAvailable,
        sunrise = timeFormatter.formatEpochSecondsToTimeString(sunrise) ?: noDataAvailable,
        sunset = timeFormatter.formatEpochSecondsToTimeString(sunset) ?: noDataAvailable,
        summary = summary ?: noDataAvailable,
        minTemp = minTemp ?: noDataAvailable,
        maxTemp = maxTemp ?: noDataAvailable
    )
}

internal fun List<DailyForecast>.mapToDailyForecastModelList(
    dateFormatter: DateFormatter,
    timeFormatter: TimeFormatter,
    noDataAvailable: String
): List<DailyForecastModel> {
    return map { it.mapToDailyForecastModel(dateFormatter, timeFormatter, noDataAvailable) }
}
