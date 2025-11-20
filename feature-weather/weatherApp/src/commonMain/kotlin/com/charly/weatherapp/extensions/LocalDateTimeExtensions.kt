package com.charly.weatherapp.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number

private const val LENGTH = 2
private const val PAD_CHAR = '0'

/**
 * Formats a LocalDateTime into "dd/MM/yyyy HH:mm:ss" format.
 */
fun LocalDateTime.formatToString(): String {
    val day = day.toString().padStart(LENGTH, PAD_CHAR)
    val month = month.number.toString().padStart(LENGTH, PAD_CHAR)
    val year = year
    val hour = hour.toString().padStart(LENGTH, PAD_CHAR)
    val minute = minute.toString().padStart(LENGTH, PAD_CHAR)
    val second = second.toString().padStart(LENGTH, PAD_CHAR)

    return "$day/$month/$year $hour:$minute:$second"
}
