package com.charly.weatherapp.di

import com.charly.core.di.coreModule
import com.charly.diqualifiers.DI_WEATHER_API_KEY
import com.charly.domain.di.domainModule
import com.charly.weatherapp.formatdata.TimeFormatter
import com.charly.weatherapp.ui.mainscreen.MainViewModel
import kotlinx.datetime.TimeZone
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val weatherAppModule = module {
    includes(domainModule)
    includes(coreModule)
    factory<TimeZone> { TimeZone.currentSystemDefault() }
    factory<TimeFormatter> { TimeFormatter(timeZone = get()) }
    viewModel {
        MainViewModel(
            weatherApiKey = get(named(DI_WEATHER_API_KEY)),
            dailyWeatherForecastUseCase = get(),
            timeFormatter = get()
        )
    }
}
