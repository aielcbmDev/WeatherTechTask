package com.charly.weatherapp.formatdata.degrees

import com.charly.weatherapp.OpenClassForMocking
import org.jetbrains.compose.resources.getString
import weathertechtask.feature_weather.weatherapp.generated.resources.Res
import weathertechtask.feature_weather.weatherapp.generated.resources.degrees_units
import weathertechtask.feature_weather.weatherapp.generated.resources.display_data_with_units_text

@OpenClassForMocking
class DegreesFormatter {

    suspend fun formatDegrees(degrees: String?): String? {
        if (degrees.isNullOrEmpty()) return null
        val units = getString(Res.string.degrees_units)
        return getString(Res.string.display_data_with_units_text, degrees, units)
    }
}
