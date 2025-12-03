package com.something.volkswagentechtask.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.charly.weatherapp.ui.detail.DetailViewIntent
import com.charly.weatherapp.ui.detail.DetailViewModel
import com.charly.weatherapp.ui.detail.screen.DetailScreen
import com.charly.weatherapp.ui.main.MainViewIntent
import com.charly.weatherapp.ui.main.MainViewModel
import com.charly.weatherapp.ui.main.screen.MainScreen
import org.koin.compose.viewmodel.koinViewModel

data object MainScreen
data class DetailScreen(val id: Long)

@Composable
fun WeatherNavigationHost() {
    val backStack = remember { mutableStateListOf<Any>(MainScreen) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<MainScreen> {
                val mainViewModel = koinViewModel<MainViewModel>()
                val mainScreenState by mainViewModel.state.collectAsStateWithLifecycle()
                MainScreen(
                    mainScreenState = mainScreenState,
                    onDailyForecastModelClick = { id ->
                        backStack.add(DetailScreen(id))
                    },
                    onRetryButtonClicked = {
                        mainViewModel.handleIntent(MainViewIntent.FetchDailyWeatherForecast(true))
                    }
                )
            }
            entry<DetailScreen> { detailScreen ->
                val detailViewModel = koinViewModel<DetailViewModel>(
                    parameters = { org.koin.core.parameter.parametersOf(detailScreen.id) }
                )
                val detailScreenState by detailViewModel.state.collectAsStateWithLifecycle()
                DetailScreen(
                    detailScreenState = detailScreenState,
                    onRetryButtonClicked = { detailViewModel.handleIntent(DetailViewIntent.FetchDailyWeatherForecastById) },
                    onBackButtonClicked = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
