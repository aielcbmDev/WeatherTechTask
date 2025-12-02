package com.something.volkswagentechtask

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.charly.uitheme.DarkColors
import com.charly.uitheme.LightColors
import com.charly.uitheme.typography
import com.charly.weatherapp.di.weatherAppModule
import com.something.volkswagentechtask.di.appModule
import com.something.volkswagentechtask.navigation.WeatherNavigationHost
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration

@Composable
@Preview
fun App(
    koinAppDeclaration: KoinAppDeclaration? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
) {
    KoinApplication(application = {
        koinAppDeclaration?.invoke(this)
        modules(appModule, weatherAppModule)
    }) {
        val colorScheme = if (darkTheme) {
            DarkColors
        } else {
            LightColors
        }
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography
        ) {
            WeatherNavigationHost()
        }
    }
}
