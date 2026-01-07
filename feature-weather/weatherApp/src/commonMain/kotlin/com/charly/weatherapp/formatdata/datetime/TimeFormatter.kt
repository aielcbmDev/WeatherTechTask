package com.charly.weatherapp.formatdata.datetime

fun interface TimeFormatter {

    fun formatEpochSecondsToTimeString(epochSeconds: Long?): String?
}
