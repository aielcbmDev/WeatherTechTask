package com.charly.weatherapp.ui.main.screen

import androidx.compose.runtime.Composable
import com.charly.weatherapp.ui.common.error.ScreenError
import com.charly.weatherapp.ui.common.loading.ScreenLoading
import com.charly.weatherapp.ui.main.MainScreenState
import com.charly.weatherapp.ui.main.MainUiState

@Composable
fun MainScreen(
    mainScreenState: MainScreenState,
    onDailyForecastModelClick: (Long) -> Unit,
    onRetryButtonClicked: () -> Unit
) {
    when (val uiState = mainScreenState.mainUiState) {
        is MainUiState.Loading -> ScreenLoading()
        is MainUiState.Success -> MainScreenSuccess(
            dailyForecastMainModelList = uiState.dailyForecastMainModelList,
            isSnackBarVisible = uiState.isSnackBarVisible,
            onDailyForecastModelClick = onDailyForecastModelClick,
            onRetryButtonClicked = onRetryButtonClicked
        )

        is MainUiState.Error -> ScreenError(onRetryButtonClicked)
    }
}
