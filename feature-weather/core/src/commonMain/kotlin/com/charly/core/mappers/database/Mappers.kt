package com.charly.core.mappers.database

import com.charly.database.model.DailyEntity
import com.charly.domain.model.Daily

internal fun DailyEntity.mapToDaily(): Daily {
    return Daily(
        id = id,
        dt = dt,
        sunrise = sunrise,
        sunset = sunset,
        summary = summary,
        minTemp = minTemp,
        maxTemp = maxTemp
    )
}

internal fun List<DailyEntity>.mapToDailyList(): List<Daily> {
    return map { it.mapToDaily() }
}
