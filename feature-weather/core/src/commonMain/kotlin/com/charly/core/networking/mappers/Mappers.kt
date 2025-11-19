package com.charly.core.networking.mappers

import com.charly.database.model.DailyEntity
import com.charly.networking.model.DailyData
import com.charly.networking.model.DailyForecastWeatherData

private fun DailyData.mapToDailyEntity(): DailyEntity {
    return DailyEntity(
        dt = dt,
        sunrise = sunrise,
        sunset = sunset,
        summary = summary,
        minTemp = temp?.min,
        maxTemp = temp?.max
    )
}

/**
 * This function is responsible for mapping the incoming DailyForecastWeatherData DTO (Data Transfer
 * Object) from the network layer to a list of DailyEntity objects for the data layer.
 *
 * This mapping stage determines which data fields are persisted and used within the app, and which
 * are discarded. For the purpose of this task, only a few key parameters are mapped to maintain
 * simplicity. This has the direct benefit of simplifying the database implementation. In a
 * production environment, this mapping would be expanded to include more fields, leading to a more
 * comprehensive data model and, consequently, a more complex database schema.
 */
internal fun DailyForecastWeatherData.mapToDailyEntityList(): List<DailyEntity> {
    return this.daily.map { it.mapToDailyEntity() }
}
