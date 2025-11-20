package com.charly.weatherapp.formatdata

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class DateFormatter(
    private val timeZone: TimeZone
) {

    @OptIn(ExperimentalTime::class)
    fun formatEpochSecondsToDateString(epochSeconds: Long?): String? {
        if (epochSeconds == null) return null
        val instant: Instant = Instant.fromEpochSeconds(epochSeconds)
        val datetime: LocalDateTime = instant.toLocalDateTime(timeZone)
        return datetime.formatToDate()
    }
}
