package com.something.volkswagentechtask.previews.weatherapp.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.charly.weatherapp.ui.main.MainScreenState
import com.charly.weatherapp.ui.main.screen.MainScreen

@Preview
@Composable
fun MainScreenPreview(
    @PreviewParameter(MainScreenParameterProvider::class) mainScreenState: MainScreenState,
    onDailyForecastModelClick: (Long) -> Unit = {},
    onRetryButtonClicked: () -> Unit = {}
) {
    MainScreen(
        mainScreenState = mainScreenState,
        onDailyForecastModelClick = onDailyForecastModelClick,
        onRetryButtonClicked = onRetryButtonClicked
    )
}
