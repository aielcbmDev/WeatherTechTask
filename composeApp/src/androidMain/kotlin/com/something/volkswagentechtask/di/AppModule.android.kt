package com.something.volkswagentechtask.di

import com.something.volkswagentechtask.WeatherApiSecrets
import com.something.volkswagentechtask.WeatherApiSecretsImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val appPlatformModule: Module
    get() = module {
        single<WeatherApiSecrets> { WeatherApiSecretsImpl() }
    }
