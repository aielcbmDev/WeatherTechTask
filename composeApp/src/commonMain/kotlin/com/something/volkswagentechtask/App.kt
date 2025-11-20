package com.something.volkswagentechtask

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charly.weatherapp.di.weatherAppModule
import com.charly.weatherapp.ui.mainscreen.MainScreen
import com.charly.weatherapp.ui.mainscreen.MainViewModel
import com.charly.weatherapp.ui.mainscreen.ViewIntent
import com.something.volkswagentechtask.di.appModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.KoinAppDeclaration

@Composable
@Preview
fun App(koinAppDeclaration: KoinAppDeclaration? = null) {
    KoinApplication(application = {
        koinAppDeclaration?.invoke(this)
        modules(appModule, weatherAppModule)
    }) {
        MaterialTheme {
            MainNavigationHost()
        }
    }
}

@Composable
private fun MainNavigationHost() {
    val mainViewModel = koinViewModel<MainViewModel>()
    val mainScreenState by mainViewModel.state.collectAsStateWithLifecycle()
    MainScreen(
        mainScreenState = mainScreenState,
        onDailyForecastModelClick = {},
        onRetryButtonClicked = { mainViewModel.handleIntent(ViewIntent.FetchDailyWeatherForecast) }
    )
}
