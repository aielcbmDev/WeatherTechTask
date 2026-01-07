package com.charly.networking.di

import com.charly.diqualifiers.DI_IS_DEBUG
import com.charly.diqualifiers.DI_WEATHER_API_KEY
import com.charly.diqualifiers.DI_WEATHER_UNITS
import com.charly.networking.WeatherApiService
import com.charly.networking.WeatherApiServiceImpl
import com.charly.networking.datasource.WeatherNetworkingDataSource
import com.charly.networking.httpclient.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal expect val networkingPlatformModule: Module

val networkingModule = module {
    includes(networkingPlatformModule)
    factory<HttpClientFactory> {
        HttpClientFactory(
            isDebug = get(named(DI_IS_DEBUG)),
            httpClientEngine = get()
        )
    }
    single<HttpClient> {
        get<HttpClientFactory>().createHttpClient()
    }
    factory<WeatherApiService> {
        WeatherApiServiceImpl(
            weatherApiKey = get(named(DI_WEATHER_API_KEY)),
            weatherUnits = get(named(DI_WEATHER_UNITS)),
            httpClient = get()
        )
    }
    factory<WeatherNetworkingDataSource> { WeatherNetworkingDataSource(get()) }
}
