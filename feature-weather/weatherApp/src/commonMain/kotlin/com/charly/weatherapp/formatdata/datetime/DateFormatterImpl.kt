package com.charly.weatherapp.formatdata.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class DateFormatterImpl(
    private val timeZone: TimeZone
) : DateFormatter {

    @OptIn(ExperimentalTime::class)
    override fun formatEpochSecondsToDateString(epochSeconds: Long?): String? {
        if (epochSeconds == null) return null
        val instant: Instant = Instant.fromEpochSeconds(epochSeconds)
        val datetime: LocalDateTime = instant.toLocalDateTime(timeZone)
        return datetime.formatToDate()
    }
}
