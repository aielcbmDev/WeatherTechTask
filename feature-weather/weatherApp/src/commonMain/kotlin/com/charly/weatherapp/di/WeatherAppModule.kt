package com.charly.weatherapp.di

import com.charly.core.di.coreModule
import com.charly.diqualifiers.DI_WEATHER_UNITS
import com.charly.domain.di.domainModule
import com.charly.weatherapp.formatdata.datetime.DateFormatter
import com.charly.weatherapp.formatdata.datetime.TimeFormatter
import com.charly.weatherapp.model.WeatherUnits
import com.charly.weatherapp.ui.detailscreen.DetailViewModel
import com.charly.weatherapp.ui.mainscreen.MainViewModel
import kotlinx.datetime.TimeZone
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val weatherAppModule = module {
    includes(domainModule)
    includes(coreModule)
    single<WeatherUnits> { WeatherUnits.METRIC }
    single<String>(named(DI_WEATHER_UNITS)) { get<WeatherUnits>().units }
    factory<TimeZone> { TimeZone.UTC }
    factory<DateFormatter> { DateFormatter(timeZone = get()) }
    factory<TimeFormatter> { TimeFormatter(timeZone = get()) }
    viewModel {
        MainViewModel(
            getDailyWeatherForecastListUseCase = get(),
            dateFormatter = get(),
            timeFormatter = get()
        )
    }
    viewModel {
        DetailViewModel(
            itemId = get(),
            getDailyWeatherForecastByIdUseCase = get(),
            dateFormatter = get(),
            timeFormatter = get()
        )
    }
}
