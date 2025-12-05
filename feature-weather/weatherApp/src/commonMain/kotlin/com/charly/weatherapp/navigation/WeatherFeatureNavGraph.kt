package com.charly.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import com.charly.navigation.DetailScreenKey
import com.charly.navigation.MainScreenKey
import com.charly.navigation.ScreenKey
import com.charly.weatherapp.ui.detail.DetailViewIntent
import com.charly.weatherapp.ui.detail.DetailViewModel
import com.charly.weatherapp.ui.detail.screen.DetailScreen
import com.charly.weatherapp.ui.main.MainViewIntent
import com.charly.weatherapp.ui.main.MainViewModel
import com.charly.weatherapp.ui.main.screen.MainScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AddWeatherFeatureEntries(
    scope: EntryProviderScope<ScreenKey>,
    backStack: MutableList<ScreenKey>
) {
    scope.entry<MainScreenKey> {
        val mainViewModel = koinViewModel<MainViewModel>()
        val mainScreenState by mainViewModel.state.collectAsStateWithLifecycle()
        MainScreen(
            mainScreenState = mainScreenState,
            onDailyForecastModelClick = { id ->
                backStack.add(DetailScreenKey(id))
            },
            onRetryButtonClicked = {
                mainViewModel.handleIntent(MainViewIntent.FetchDailyWeatherForecast(true))
            }
        )
    }
    scope.entry<DetailScreenKey> { detailScreen ->
        val detailViewModel = koinViewModel<DetailViewModel>(
            parameters = { parametersOf(detailScreen.id) }
        )
        val detailScreenState by detailViewModel.state.collectAsStateWithLifecycle()
        DetailScreen(
            detailScreenState = detailScreenState,
            onRetryButtonClicked = { detailViewModel.handleIntent(DetailViewIntent.FetchDailyWeatherForecastById) },
            onBackButtonClicked = { backStack.removeLastOrNull() }
        )
    }
}
