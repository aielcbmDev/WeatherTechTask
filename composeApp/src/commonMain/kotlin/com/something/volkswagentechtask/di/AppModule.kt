package com.something.volkswagentechtask.di

import com.charly.weatherapp.ui.MainViewModel
import com.something.volkswagentechtask.WeatherApiSecrets
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal expect val appPlatformModule: Module

val appModule = module {
    includes(appPlatformModule)
    single(named("WEATHER_API_KEY")) {
        get<WeatherApiSecrets>().getWeatherApiKey()
    }
    viewModel {
        MainViewModel(
            weatherApiKey = get(named("WEATHER_API_KEY")),
        )
    }
}
