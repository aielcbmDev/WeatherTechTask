package com.something.volkswagentechtask.di

import com.charly.diqualifiers.DI_IS_DEBUG
import com.charly.diqualifiers.DI_WEATHER_API_KEY
import com.charly.weatherapp.ui.MainViewModel
import com.something.volkswagentechtask.apisecrets.WeatherApiSecrets
import com.something.volkswagentechtask.utils.BuildConfig
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal expect val appPlatformModule: Module

val appModule = module {
    includes(appPlatformModule)
    single(named(DI_WEATHER_API_KEY)) {
        get<WeatherApiSecrets>().getWeatherApiKey()
    }
    single(named(DI_IS_DEBUG)) {
        get<BuildConfig>().isDebug()
    }
    viewModel {
        MainViewModel(
            weatherApiKey = get(named(DI_WEATHER_API_KEY)),
            dailyWeatherForecastUseCase = get()
        )
    }
}
