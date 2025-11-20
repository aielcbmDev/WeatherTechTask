package com.charly.weatherapp.formatdata

import com.charly.weatherapp.extensions.formatToString
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class TimeFormatter(
    private val timeZone: TimeZone
) {

    @OptIn(ExperimentalTime::class)
    fun formatEpochSecondsToString(epochSeconds: Long?): String? {
        if (epochSeconds == null) return null
        val instant: Instant = Instant.fromEpochSeconds(epochSeconds)
        val datetime: LocalDateTime = instant.toLocalDateTime(timeZone)
        return datetime.formatToString()
    }
}
