package com.charly.weatherapp.provider

import com.charly.weatherapp.OpenClassForMocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

@OpenClassForMocking
class StringProvider {

    suspend fun getStringForResource(resource: StringResource): String {
        return getString(resource)
    }
}
