package com.charly.core.mappers.networking

import com.charly.database.model.DailyForecastEntity
import com.charly.networking.model.DailyForecastData
import com.charly.networking.model.DailyForecastWeatherData

private fun DailyForecastData.mapToDailyEntity(): DailyForecastEntity {
    return DailyForecastEntity(
        dt = dt,
        sunrise = sunrise,
        sunset = sunset,
        summary = summary,
        minTemp = temp?.min,
        maxTemp = temp?.max
    )
}

/**
 * This function is responsible for mapping the incoming DailyForecastWeatherData from the network
 * module to a list of DailyEntity objects for the database module.
 *
 * This mapping stage determines which data fields are persisted and used within the app, and which
 * are discarded. For the purpose of this task, only a few key parameters are mapped to maintain
 * simplicity. This has the direct benefit of simplifying the database implementation. In a
 * production environment, this mapping would be expanded to include more fields, leading to a more
 * comprehensive data model and, consequently, a more complex database schema.
 */
internal fun DailyForecastWeatherData.mapToDailyEntityList(): List<DailyForecastEntity> {
    return this.daily.map { it.mapToDailyEntity() }
}
